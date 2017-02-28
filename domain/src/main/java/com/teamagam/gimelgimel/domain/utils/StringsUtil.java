package com.teamagam.gimelgimel.domain.utils;


import java.util.Collection;

public class StringsUtil {

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String join(Collection<String> strings, String separator) {
        String[] alerts = new String[strings.size()];
        strings.toArray(alerts);
        StringBuilder sb = new StringBuilder();
        sb.append(alerts[0]);
        for (int i = 1; i < alerts.length; i++) {
            sb.append(separator).append(alerts[i]);
        }
        return sb.toString();
    }
}
