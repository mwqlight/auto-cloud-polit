package com.company.service;

import com.company.plugin.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * 插件管理服务
 */
@Service
public class PluginService {
    
    @Autowired
    private PluginManager pluginManager;
    
    /**
     * 初始化插件管理器
     */
    @PostConstruct
    public void initPlugins() {
        try {
            pluginManager.loadPlugins();
            System.out.println("All plugins loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load plugins: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 启动插件
     */
    public void startPlugins() {
        try {
            pluginManager.startPlugins();
            System.out.println("All plugins started successfully");
        } catch (Exception e) {
            System.err.println("Failed to start plugins: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取插件管理器
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
}