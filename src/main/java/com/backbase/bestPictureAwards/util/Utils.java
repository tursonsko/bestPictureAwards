package com.backbase.bestPictureAwards.util;

public final class Utils {

    public static String EMPTY_STRING = "";

    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String replaceSpacesInRequestParamValue(String text) {
        if (isStringNullOrEmpty(text)) {
            return EMPTY_STRING;
        }
        return text.replaceAll("\\s+","+");
    }

    public static int parseValueToNumeric(String value) {
        if (isStringNullOrEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value.replaceAll("[^0-9]", EMPTY_STRING));
    }

}
