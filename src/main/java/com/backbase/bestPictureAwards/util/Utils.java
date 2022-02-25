package com.backbase.bestPictureAwards.util;

import java.util.regex.Pattern;

public final class Utils {

    public static final String EMPTY_STRING = "";
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMERIC_PATTERN.matcher(strNum).matches();
    }

    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.length() == 0;
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

        String parsedValue = value.replaceAll("[^0-9]", EMPTY_STRING);
        if (isNumeric(parsedValue)) {
            return Integer.parseInt(parsedValue.replaceAll("[^0-9]", EMPTY_STRING));
        }

        return 0;

    }

}
