package com.company.plugin;

import com.company.spi.PluginLifecycle;

import java.io.File;
import java.net.URLClassLoader;

/**
 * 插件信息类
 */
public class PluginInfo {
    
    // 插件ID
    private final String id;
    
    // 插件文件
    private final File file;
    
    // 类加载器
    private final URLClassLoader classLoader;
    
    // 插件生命周期管理器
    private PluginLifecycle pluginLifecycle;
    
    public PluginInfo(String id, File file, URLClassLoader classLoader) {
        this.id = id;
        this.file = file;
        this.classLoader = classLoader;
    }
    
    public String getId() {
        return id;
    }
    
    public File getFile() {
        return file;
    }
    
    public URLClassLoader getClassLoader() {
        return classLoader;
    }
    
    public PluginLifecycle getPluginLifecycle() {
        return pluginLifecycle;
    }
    
    public void setPluginLifecycle(PluginLifecycle pluginLifecycle) {
        this.pluginLifecycle = pluginLifecycle;
    }
}