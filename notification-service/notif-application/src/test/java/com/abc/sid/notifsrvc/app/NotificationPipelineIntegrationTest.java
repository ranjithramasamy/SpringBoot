package com.abc.sid.notifsrvc.app;

import com.abc.sid.notifsrvc.app.persistence.repository.AuditJpaRepository;
import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.engine.orchestrator.XMLTransformationOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationPipelineIntegrationTest {

    @Autowired
    private XMLTransformationOrchestrator orchestrator;

    @Autowired
    private AuditJpaRepository auditRepository;

    @Test
    void shouldTransformValidateAndAuditCanoMessageEndToEnd() {
        String validCanoJson = """
                {
                  "messageId": "MSG-CANO-1001",
                  "eventId": "EVT-CA-55001",
                  "messageType": "CANO",
                  "messageVersion": "1.0",
                  "workflowStatus": "AMND",
                  "security": {
                    "isin": "US0378331005",
                    "description": "APPLE INC COM"
                  },
                  "details": {
                    "caNumber": "CA-55001",
                    "eventTypeCode": "DVCA",
                    "mandatoryVoluntary": "MAND",
                    "paymentDate": "2026-12-01"
                  },
                  "rates": {
                    "grossDividendRate": 0.25,
                    "currency": "USD"
                  }
                }
                """;

        TransformationContext ctx = orchestrator.orchestrate(validCanoJson);

        assertThat(ctx.hasErrors()).isFalse();
        assertThat(ctx.getFinalStatus()).isEqualTo(AuditStatus.PUBLISHED);
        assertThat(ctx.getGeneratedXmlAsString())
                .contains("<NtfctnId>MSG-CANO-1001</NtfctnId>")
                .contains("<NtfctnTp><Cd>AMND</Cd></NtfctnTp>")
                .contains("<ISIN>US0378331005</ISIN>")
                .contains("<Amt Ccy=\"USD\">0.25</Amt>");

        var auditEntity = auditRepository.findByMessageId("MSG-CANO-1001").orElseThrow();
        assertThat(auditEntity.getStatus()).isEqualTo("PUBLISHED");
        assertThat(auditEntity.getProfileId()).isEqualTo("PROF-CANO-V1");

        // Second submission of same messageId must short-circuit via IdempotencyStage
        TransformationContext duplicateCtx = orchestrator.orchestrate(validCanoJson);
        assertThat(duplicateCtx.isShortCircuited()).isTrue();
        assertThat(duplicateCtx.getFinalStatus()).isEqualTo(AuditStatus.DUPLICATE_SKIPPED);
    }

    @Test
    void shouldHaltAndAuditFailureWhenDroolsRuleViolated() {
        String invalidIsinJson = """
                {
                  "messageId": "MSG-CANO-ERR-01",
                  "eventId": "EVT-CA-55002",
                  "messageType": "CANO",
                  "messageVersion": "1.0",
                  "security": {
                    "isin": "BAD_ISIN"
                  },
                  "details": {
                    "caNumber": "CA-55002",
                    "eventTypeCode": "DVCA",
                    "mandatoryVoluntary": "MAND"
                  },
                  "rates": {
                    "grossDividendRate": -5.0,
                    "currency": "USD"
                  }
                }
                """;

        TransformationContext ctx = orchestrator.orchestrate(invalidIsinJson);

        assertThat(ctx.hasErrors()).isTrue();
        assertThat(ctx.getFinalStatus()).isEqualTo(AuditStatus.FAILED);
        assertThat(ctx.getErrors()).anyMatch(err -> err.contains("CANO-001"));
        assertThat(ctx.getErrors()).anyMatch(err -> err.contains("CANO-003"));

        var auditEntity = auditRepository.findByMessageId("MSG-CANO-ERR-01").orElseThrow();
        assertThat(auditEntity.getStatus()).isEqualTo("FAILED");
        assertThat(auditEntity.getErrorReason()).contains("XMLTransformationStage");
    }

    @Test
    void shouldOmitEmptyOptionalXmlParentContainersWhenConditionalChildExcluded() {
        String nonDividendCanoJson = """
                {
                  "messageId": "MSG-CANO-1002",
                  "eventId": "EVT-CA-55003",
                  "messageType": "CANO",
                  "messageVersion": "1.0",
                  "security": {
                    "localId": "037833100"
                  },
                  "details": {
                    "caNumber": "CA-55003",
                    "eventTypeCode": "SPLF",
                    "mandatoryVoluntary": "MAND"
                  }
                }
                """;

        TransformationContext ctx = orchestrator.orchestrate(nonDividendCanoJson);

        assertThat(ctx.hasErrors()).isFalse();
        assertThat(ctx.getFinalStatus()).isEqualTo(AuditStatus.PUBLISHED);
        // ISIN omitted, OthrId included; RateAndAmtDtls completely suppressed so XSD validation passes
        assertThat(ctx.getGeneratedXmlAsString())
                .doesNotContain("<ISIN>")
                .contains("<OthrId><Id>037833100</Id></OthrId>")
                .doesNotContain("<RateAndAmtDtls>");
    }
}
