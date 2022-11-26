package me.efco.data;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class PropertiesLoader {
    private static PropertiesLoader instance;
    private Properties properties;

    private PropertiesLoader() {
        properties = new Properties();

        try (InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public String getProperty(String propertyName) {
        if (properties.stringPropertyNames().contains(propertyName)) {
            return properties.getProperty(propertyName);
        }

        return null;
    }

    public void setProperty(String configName, String configValue) {
        properties.setProperty(configName, configValue);
    }

    public void save() {
        try {
            properties.store(new FileOutputStream("config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }

        return instance;
    }
}
