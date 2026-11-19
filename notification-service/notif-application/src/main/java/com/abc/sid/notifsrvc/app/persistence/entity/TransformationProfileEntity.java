package com.abc.sid.notifsrvc.app.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CA_TRANSFORMATION_PROFILE")
public class TransformationProfileEntity {

    @Id
    @Column(name = "PROFILE_ID", length = 36)
    private String profileId;

    @Column(name = "MESSAGE_TYPE", nullable = false, length = 32)
    private String messageType;

    @Column(name = "MESSAGE_VERSION", nullable = false, length = 16)
    private String messageVersion;

    @Column(name = "TARGET_ISO_TYPE", nullable = false, length = 32)
    private String targetIsoType;

    @Column(name = "MAPPING_S3_KEY", nullable = false, length = 256)
    private String mappingS3Key;

    @Column(name = "RULES_S3_KEY", nullable = false, length = 256)
    private String rulesS3Key;

    @Column(name = "XSD_S3_KEY", nullable = false, length = 256)
    private String xsdS3Key;

    @Column(name = "ACTIVE_FLAG", nullable = false)
    private Integer activeFlag;

    public String getProfileId() { return profileId; }
    public String getMessageType() { return messageType; }
    public String getMessageVersion() { return messageVersion; }
    public String getTargetIsoType() { return targetIsoType; }
    public String getMappingS3Key() { return mappingS3Key; }
    public String getRulesS3Key() { return rulesS3Key; }
    public String getXsdS3Key() { return xsdS3Key; }
    public Integer getActiveFlag() { return activeFlag; }
}
