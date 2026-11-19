package com.abc.sid.notifsrvc.engine.mapping;

import com.abc.sid.notifsrvc.common.util.SafeSpelEvaluator;
import com.abc.sid.notifsrvc.engine.mapping.CompiledYamlXmlMapper.SwitchBranch;
import com.abc.sid.notifsrvc.engine.mapping.CompiledYamlXmlMapper.XmlNodeAst;
import com.ctc.wstx.stax.WstxOutputFactory;
import org.springframework.expression.Expression;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiles a declarative YAML mapping definition once into an immutable, thread-safe CompiledYamlXmlMapper AST.
 */
public final class YamlAstCompiler {

    private YamlAstCompiler() {}

    @SuppressWarnings("unchecked")
    public static CompiledYamlXmlMapper compile(String yamlContent, WstxOutputFactory outputFactory) {
        Yaml yaml = new Yaml();
        Map<String, Object> doc = yaml.load(yamlContent);
        if (doc == null || !doc.containsKey("root")) {
            throw new IllegalArgumentException("Invalid YAML mapping: missing 'root' node");
        }
        String targetNamespace = (String) doc.get("targetNamespace");
        Map<String, Object> rootMap = (Map<String, Object>) doc.get("root");
        XmlNodeAst rootNode = parseNode(rootMap);
        return new CompiledYamlXmlMapper(outputFactory, targetNamespace, rootNode);
    }

    @SuppressWarnings("unchecked")
    private static XmlNodeAst parseNode(Map<String, Object> nodeMap) {
        String name = (String) nodeMap.get("name");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("YAML XML node missing required 'name'");
        }

        String whenStr = (String) nodeMap.get("when");
        Expression whenExpr = (whenStr != null && !whenStr.isBlank()) ? SafeSpelEvaluator.compile(whenStr) : null;

        String valueFrom = (String) nodeMap.get("valueFrom");

        String valueStr = (String) nodeMap.get("value");
        Expression literalValueExpr = (valueStr != null && !valueStr.isBlank()) ? SafeSpelEvaluator.compile(valueStr) : null;

        List<SwitchBranch> switchBranches = parseSwitchBranches((List<Map<String, Object>>) nodeMap.get("switch"));

        Map<String, String> attributes = null;
        if (nodeMap.get("attributes") instanceof Map<?, ?> rawAttrs) {
            attributes = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : rawAttrs.entrySet()) {
                attributes.put(entry.getKey().toString(), entry.getValue().toString());
            }
            attributes = Collections.unmodifiableMap(attributes);
        }

        List<XmlNodeAst> children = List.of();
        if (nodeMap.get("children") instanceof List<?> rawChildren) {
            List<XmlNodeAst> parsedChildren = new ArrayList<>(rawChildren.size());
            for (Object childObj : rawChildren) {
                if (childObj instanceof Map<?, ?> childMap) {
                    parsedChildren.add(parseNode((Map<String, Object>) childMap));
                }
            }
            children = Collections.unmodifiableList(parsedChildren);
        }

        return new XmlNodeAst(
                name,
                whenExpr,
                valueFrom,
                literalValueExpr,
                switchBranches,
                attributes,
                children
        );
    }

    private static List<SwitchBranch> parseSwitchBranches(List<Map<String, Object>> rawSwitch) {
        if (rawSwitch == null || rawSwitch.isEmpty()) {
            return null;
        }
        List<SwitchBranch> branches = new ArrayList<>(rawSwitch.size());
        for (Map<String, Object> branchMap : rawSwitch) {
            if (branchMap.containsKey("default")) {
                String defaultVal = branchMap.get("default").toString();
                branches.add(new SwitchBranch(null, SafeSpelEvaluator.compile(defaultVal)));
            } else {
                String whenStr = (String) branchMap.get("when");
                String valStr = branchMap.get("value").toString();
                Expression cond = (whenStr != null && !whenStr.isBlank()) ? SafeSpelEvaluator.compile(whenStr) : null;
                branches.add(new SwitchBranch(cond, SafeSpelEvaluator.compile(valStr)));
            }
        }
        return Collections.unmodifiableList(branches);
    }
}
