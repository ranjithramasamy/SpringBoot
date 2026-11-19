# Notification Service (`com.abc.sid.notifsrvc`)

Enterprise-grade, multi-module Maven Spring Boot 4.x application (Java 21) for transforming JSON Corporate Action notifications (`CANO`, `CAPA`, etc.) into XSD-validated ISO 20022 XML messages (`seev.*`) for SWIFT delivery.

## Multi-Module Architecture

1. **`notif-common`**: Shared constants, domain/system exception hierarchy, utility helpers (zero-allocation dot-path resolver, safe SpEL evaluator, Woodstox StAX wrapper, SHA-256 checksum, PII data masker), and common Spring configuration.
2. **`notif-domain`**: Canonical Protobuf contract (`corporate_action.proto`), DDD Value Objects (`MessageId`, `EventId`, `MessageType`), Aggregate Root (`TransformationContext`), `PipelineStage` contract, and hexagonal Domain Ports.
3. **`notif-core-engine`**:
   - `XMLTransformationOrchestrator` (Chain of Responsibility)
   - `IntakeValidationStage` (JSON -> Typed Protobuf + mandatory validation)
   - `IdempotencyStage` (Duplicate gate via `CA_TRANSFORMATION_AUDIT`)
   - `ProfileResolutionStage` (Caffeine-cached S3 `.yml`, `.drl`, `.xsd` bundle resolution)
   - `XMLTransformationStage` (Enrichment + Drools `.drl` business rule execution + Compiled YAML AST & Woodstox StAX XML streaming)
   - `SchemaValidationStage` (Thread-safe XSD validation)
   - `AuditAndPublishStage` (Kafka emission keyed by `eventId` + Audit persistence)
4. **`notif-application`**: Spring Boot 4.x executable application (`NotificationServiceApplication`), H2 (`MODE=Oracle`) / Oracle JPA repositories, AWS S3 + classpath fallback artifact storage adapter, Kafka consumer/producer adapters, REST test endpoint, and JUnit 5 integration tests.

## Quick Start

```bash
# Build all modules and run unit + integration tests
mvn clean test

# Run the Spring Boot application (starts with H2 Oracle-mode & local artifact fallback)
mvn -pl notif-application spring-boot:run
```

### Sample HTTP Test Request

```bash
curl -X POST http://localhost:8080/api/v1/notifications/transform \
  -H "Content-Type: application/json" \
  -d '{
    "messageId": "MSG-20261119-001",
    "eventId": "EVT-CA-998877",
    "messageType": "CANO",
    "messageVersion": "1.0",
    "workflowStatus": "NEWM",
    "security": {
      "isin": "US0378331005",
      "description": "APPLE INC COM"
    },
    "details": {
      "caNumber": "CA-998877",
      "eventTypeCode": "DVCA",
      "mandatoryVoluntary": "MAND",
      "paymentDate": "2026-12-01"
    },
    "rates": {
      "grossDividendRate": 0.25,
      "currency": "USD"
    }
  }'
```
