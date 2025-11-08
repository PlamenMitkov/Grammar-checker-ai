package com.grammarchecker.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration loader for environment variables
 */
public class Config {
    private static Properties properties = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try {
            // Try to load .env file
            FileInputStream fis = new FileInputStream(".env");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Warning: .env file not found. Please create one based on .env.example");
        }
    }
    
    public static String getOpenAIApiKey() {
        return properties.getProperty("OPENAI_API_KEY", "");
    }
    
    public static String getOpenAIModel() {
        return properties.getProperty("OPENAI_MODEL", "gpt-4o-mini");
    }
    
    public static int getMaxTokens() {
        return Integer.parseInt(properties.getProperty("MAX_TOKENS", "4000"));
    }
}
