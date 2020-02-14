package com.example.utils;

public class GeneralHelper {
    public static boolean isNullOrEmpty(Object str) {
        try {
            String parsedStr = str.toString();
            return parsedStr.trim().isEmpty();
        } catch (Exception ex) {
            //unable to parse to string
            return true;
        }
    }
}
