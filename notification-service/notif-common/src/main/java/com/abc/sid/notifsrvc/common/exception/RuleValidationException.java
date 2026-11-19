package com.abc.sid.notifsrvc.common.exception;

import java.util.List;

public class RuleValidationException extends NotificationServiceException {

    private final List<String> violations;

    public RuleValidationException(List<String> violations) {
        super("ERR_BUSINESS_RULE_VIOLATION", String.join("; ", violations));
        this.violations = List.copyOf(violations);
    }

    public List<String> getViolations() {
        return violations;
    }
}
