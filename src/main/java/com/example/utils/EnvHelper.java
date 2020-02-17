package com.example.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvHelper {
    private static Dotenv dotenv;

    public static String get(String key) {
        if (dotenv == null) {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }

        return dotenv.get(key);
    }
}
