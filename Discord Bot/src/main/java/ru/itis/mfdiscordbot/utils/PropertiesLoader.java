package ru.itis.mfdiscordbot.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesLoader {
    private static final String FILE_NAME = "app.properties";

    private static PropertiesLoader instance;
    private final Properties properties;


    private PropertiesLoader() {
        properties = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME),StandardCharsets.UTF_8);
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.warn("Cannot load properties file. " + e.getMessage());
        }
    }

    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
