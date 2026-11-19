package com.abc.sid.notifsrvc.common.util;

import java.util.regex.Pattern;

public final class SensitiveDataMasker {

    private static final Pattern ACCOUNT_PATTERN =
            Pattern.compile("(<SfkpgAcct>)([^<]+)(</SfkpgAcct>)");

    private SensitiveDataMasker() {}

    public static String maskXmlForLogging(String xml) {
        if (xml == null || xml.isBlank()) {
            return "";
        }
        return ACCOUNT_PATTERN.matcher(xml).replaceAll("$1****MASKED****$3");
    }
}
