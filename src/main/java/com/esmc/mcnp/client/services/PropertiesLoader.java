package com.esmc.mcnp.client.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public PropertiesLoader() {
    }

    public static Properties loadProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            configuration.load(inputStream);
        }
        return configuration;
    }

    public static String getProperty(String property) {
        Properties props;
        try {
            props = loadProperties("application.properties");
            return props.getProperty(property);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static void setProperty(String property, String value) {
        Properties props;
        try {
            props = loadProperties("application.properties");
            props.setProperty(property, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
