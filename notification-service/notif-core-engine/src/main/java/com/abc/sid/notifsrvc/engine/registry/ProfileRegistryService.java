package com.abc.sid.notifsrvc.engine.registry;

import com.abc.sid.notifsrvc.common.exception.ProfileNotFoundException;
import com.abc.sid.notifsrvc.domain.model.ProfileDefinition;
import com.abc.sid.notifsrvc.domain.port.ArtifactStoragePort;
import com.abc.sid.notifsrvc.domain.port.ProfileRepositoryPort;
import com.abc.sid.notifsrvc.engine.mapping.CompiledYamlXmlMapper;
import com.abc.sid.notifsrvc.engine.mapping.YamlAstCompiler;
import com.abc.sid.notifsrvc.engine.rules.DroolsRuleEngine;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.kie.api.KieBase;
import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.time.Duration;

/**
 * Resolves profile definitions from DB and compiles/caches immutable runtime artifacts
 * (CompiledYamlXmlMapper, Drools KieBase, and XXE-hardened XSD Schema) in Caffeine.
 */
@Service
public class ProfileRegistryService {

    public record ResolvedProfileBundle(
            ProfileDefinition definition,
            CompiledYamlXmlMapper xmlMapper,
            KieBase droolsKieBase,
            Schema xsdSchema
    ) {}

    private final ProfileRepositoryPort profileRepository;
    private final ArtifactStoragePort artifactStorage;
    private final DroolsRuleEngine droolsRuleEngine;
    private final WstxOutputFactory wstxOutputFactory;
    private final Cache<String, ResolvedProfileBundle> bundleCache;

    public ProfileRegistryService(
            ProfileRepositoryPort profileRepository,
            ArtifactStoragePort artifactStorage,
            DroolsRuleEngine droolsRuleEngine,
            WstxOutputFactory wstxOutputFactory) {
        this.profileRepository = profileRepository;
        this.artifactStorage = artifactStorage;
        this.droolsRuleEngine = droolsRuleEngine;
        this.wstxOutputFactory = wstxOutputFactory;
        this.bundleCache = Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(Duration.ofHours(6))
                .build();
    }

    public ResolvedProfileBundle resolve(String messageType, String messageVersion) {
        String cacheKey = messageType + ":" + messageVersion;
        return bundleCache.get(cacheKey, k -> loadAndCompile(messageType, messageVersion));
    }

    public void evictAll() {
        bundleCache.invalidateAll();
    }

    private ResolvedProfileBundle loadAndCompile(String messageType, String messageVersion) {
        ProfileDefinition definition = profileRepository.findActiveProfile(messageType, messageVersion)
                .orElseThrow(() -> new ProfileNotFoundException(messageType, messageVersion));

        String yamlContent = artifactStorage.loadTextArtifact(definition.mappingS3Key());
        String drlContent = artifactStorage.loadTextArtifact(definition.rulesS3Key());
        String xsdContent = artifactStorage.loadTextArtifact(definition.xsdS3Key());

        CompiledYamlXmlMapper mapper = YamlAstCompiler.compile(yamlContent, wstxOutputFactory);
        KieBase kieBase = droolsRuleEngine.compileRules(drlContent, definition.rulesS3Key());
        Schema xsdSchema = compileXsdSecurely(xsdContent);

        return new ResolvedProfileBundle(definition, mapper, kieBase, xsdSchema);
    }

    private Schema compileXsdSecurely(String xsdContent) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return factory.newSchema(new StreamSource(new StringReader(xsdContent)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to compile XSD schema securely", e);
        }
    }
}
