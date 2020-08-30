package com.swia.iabuilder.utils;

public class StringUtils {

    public static String getFirstWord(String str) {
        return getFirstWord(str, " ");
    }

    public static String getFirstWord(String str, String separator) {
        return str.split(separator)[0];
    }

    public static String getLastWord(String str, String separator) {
        String[] words = str.split(separator);
        return words[words.length - 1];
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getInitials(String str) {
        StringBuilder sb = new StringBuilder();
        String[] words = str.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(word.charAt(0));
            }
        }
        return sb.toString();
    }

}
