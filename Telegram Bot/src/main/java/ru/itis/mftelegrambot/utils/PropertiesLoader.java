package ru.itis.mftelegrambot.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class PropertiesLoader {
    private static final String FILE_NAME = "app.properties";

    private static PropertiesLoader instance;
    private Properties properties;

    private Logger logger;

    private PropertiesLoader() {
        logger = Logger.getLogger(this.getClass());
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        logger.log(Level.DEBUG, "Get value from key " + key);
        return properties.getProperty(key);
    }
}
