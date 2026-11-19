package com.abc.sid.notifsrvc.common.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.Map;

/**
 * Thread-safe SpEL evaluator pinned to SpelCompilerMode.OFF to prevent dynamic classloader Metaspace leaks
 * during metadata hot-reloads.
 */
public final class SafeSpelEvaluator {

    private static final ExpressionParser PARSER = new SpelExpressionParser(
            new SpelParserConfiguration(SpelCompilerMode.OFF, null)
    );

    private static final EvaluationContext READ_ONLY_CONTEXT = SimpleEvaluationContext
            .forPropertyAccessors(DataBindingPropertyAccessor.forReadOnlyAccess())
            .build();

    private SafeSpelEvaluator() {}

    public static Expression compile(String expressionText) {
        return PARSER.parseExpression(expressionText);
    }

    public static boolean evaluateBoolean(Expression expression, Map<String, Object> payload) {
        if (expression == null) {
            return true;
        }
        Boolean result = expression.getValue(READ_ONLY_CONTEXT, Map.of("root", payload), Boolean.class);
        return Boolean.TRUE.equals(result);
    }

    public static Object evaluateValue(Expression expression, Map<String, Object> payload) {
        if (expression == null) {
            return null;
        }
        return expression.getValue(READ_ONLY_CONTEXT, Map.of("root", payload));
    }
}
