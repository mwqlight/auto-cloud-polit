package com.company.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 插件配置加载器
 */
public class PluginConfigLoader {
    
    private static final String PLUGIN_CONFIG_FILE = "plugin.properties";
    
    /**
     * 从插件JAR文件中加载配置
     */
    public static Properties loadConfigFromJar(File jarFile) throws IOException {
        Properties properties = new Properties();
        
        try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile))) {
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                if (PLUGIN_CONFIG_FILE.equals(entry.getName())) {
                    properties.load(jarInputStream);
                    break;
                }
            }
        }
        
        return properties;
    }
    
    /**
     * 从类路径加载配置
     */
    public static Properties loadConfigFromClasspath() throws IOException {
        Properties properties = new Properties();
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(PLUGIN_CONFIG_FILE);
        
        if (resource != null) {
            try (InputStream inputStream = resource.openStream()) {
                properties.load(inputStream);
            }
        }
        
        return properties;
    }
}