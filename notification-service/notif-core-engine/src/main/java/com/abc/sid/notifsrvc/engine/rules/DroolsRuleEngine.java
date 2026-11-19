package com.abc.sid.notifsrvc.engine.rules;

import com.abc.sid.notifsrvc.domain.proto.CorporateActionEvent;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiles Drools DRL files into thread-safe KieBase singletons and evaluates business rules
 * using lightweight StatelessKieSessions.
 */
@Component
public class DroolsRuleEngine {

    public KieBase compileRules(String drlContent, String sourceName) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drlContent, ResourceType.DRL);
        var results = kieHelper.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("Drools DRL compilation errors in [" + sourceName + "]: " + results.getMessages());
        }
        return kieHelper.build();
    }

    public List<String> validate(KieBase kieBase, CorporateActionEvent event) {
        List<String> violations = new ArrayList<>();
        StatelessKieSession session = kieBase.newStatelessKieSession();
        session.setGlobal("violations", violations);
        session.execute(event);
        return violations;
    }
}
