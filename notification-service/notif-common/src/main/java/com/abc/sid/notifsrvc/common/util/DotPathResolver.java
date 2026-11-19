package com.abc.sid.notifsrvc.common.util;

import java.util.Map;

/**
 * Zero-regex, zero-reflection nested Map navigator for dot-separated paths (e.g., "details.ca_number").
 */
public final class DotPathResolver {

    private DotPathResolver() {}

    public static Object resolve(Map<String, Object> root, String dotPath) {
        if (root == null || dotPath == null || dotPath.isEmpty()) {
            return null;
        }
        Object current = root;
        int start = 0;
        int dotIndex;
        while ((dotIndex = dotPath.indexOf('.', start)) != -1) {
            if (!(current instanceof Map<?, ?> map)) {
                return null;
            }
            current = map.get(dotPath.substring(start, dotIndex));
            if (current == null) {
                return null;
            }
            start = dotIndex + 1;
        }
        if (current instanceof Map<?, ?> map) {
            return map.get(dotPath.substring(start));
        }
        return null;
    }
}
