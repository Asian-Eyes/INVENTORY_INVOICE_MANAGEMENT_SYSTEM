package org.example.util;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public final class Config {
    private static Map<String, Object> config;

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (inputStream == null) {
                // If not in classpath, try to find in current directory
                try (java.io.FileInputStream fis = new java.io.FileInputStream("config.yaml")) {
                    Yaml yaml = new Yaml();
                    config = yaml.load(fis);
                }
            } else {
                Yaml yaml = new Yaml();
                config = yaml.load(inputStream);
            }
        } catch (Exception e) {
            System.err.println("Fatal: Could not load config.yaml");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Config() {
    }

    /**
     * Get a configuration value using dot notation (e.g., "db.host").
     * 
     * @param path The path to the configuration value
     * @return The value
     * @throws RuntimeException if the path is not found
     */
    @SuppressWarnings("unchecked")
    public static Object getRequired(String path) {
        String[] keys = path.split("\\.");
        Object current = config;
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                throw new RuntimeException("Missing required configuration: " + path);
            }
        }
        if (current == null) {
            throw new RuntimeException("Missing required configuration: " + path);
        }
        return current;
    }

    public static String getDbHost() {
        return getRequired("db.host").toString();
    }

    public static int getDbPort() {
        Object port = getRequired("db.port");
        if (port instanceof Number number) {
            return number.intValue();
        }
        throw new RuntimeException("Configuration db.port must be a number");
    }

    public static String getDbName() {
        return getRequired("db.name").toString();
    }

    public static String getDbUser() {
        return getRequired("db.user").toString();
    }

    public static String getDbPassword() {
        return getRequired("db.password").toString();
    }
}
