package com.abc.sid.notifsrvc.app.storage;

import com.abc.sid.notifsrvc.domain.port.ArtifactStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Fetches YAML mapping files, Drools DRL rules, and XSDs from AWS S3,
 * with configurable classpath fallback for local H2 execution and integration tests.
 */
@Component
public class S3ArtifactStorageAdapter implements ArtifactStoragePort {

    private final S3Client s3Client;
    private final String bucketName;
    private final boolean localFallbackEnabled;

    public S3ArtifactStorageAdapter(
            @Autowired(required = false) S3Client s3Client,
            @Value("${app.s3.bucket-name:corp-actions-metadata}") String bucketName,
            @Value("${app.s3.local-fallback-enabled:true}") boolean localFallbackEnabled) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.localFallbackEnabled = localFallbackEnabled;
    }

    @Override
    public String loadTextArtifact(String storageKey) {
        if (localFallbackEnabled) {
            ClassPathResource resource = new ClassPathResource(storageKey);
            if (resource.exists()) {
                try {
                    return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed reading classpath artifact: " + storageKey, e);
                }
            }
        }
        if (s3Client == null) {
            throw new IllegalStateException("Artifact not found in classpath and S3Client is disabled: " + storageKey);
        }
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();
        return s3Client.getObjectAsBytes(request).asUtf8String();
    }
}
