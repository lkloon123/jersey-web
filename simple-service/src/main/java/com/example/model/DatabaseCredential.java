package com.example.model;

public class DatabaseCredential {
    private static String host;
    private static String port;
    private static String user;
    private static String password;
    private static String database;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        DatabaseCredential.host = host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        DatabaseCredential.port = port;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DatabaseCredential.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DatabaseCredential.password = password;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        DatabaseCredential.database = database;
    }
}
