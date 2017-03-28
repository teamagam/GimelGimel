package com.teamagam.gimelgimel.domain.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static String trim(String str) {
        return str
                .trim()
                .replace("\n", "")
                .replace("\r", "");
    }

    public static boolean isOnlyWhiteSpaces(String str) {
        return str.trim().length() <= 0;
    }
}
