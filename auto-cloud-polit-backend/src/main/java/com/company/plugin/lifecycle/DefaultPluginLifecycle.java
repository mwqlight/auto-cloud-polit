package com.company.plugin.lifecycle;

import com.company.plugin.PluginManager;
import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 默认插件生命周期实现
 */
public class DefaultPluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private boolean initialized = false;
    private boolean started = false;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.initialized = true;
        System.out.println("Plugin initialized: " + context.getPluginId());
    }
    
    @Override
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("Plugin must be initialized before starting");
        }
        this.started = true;
        System.out.println("Plugin started: " + context.getPluginId());
    }
    
    @Override
    public void stop() {
        if (started) {
            this.started = false;
            System.out.println("Plugin stopped: " + context.getPluginId());
        }
    }
    
    @Override
    public void destroy() {
        if (initialized) {
            this.initialized = false;
            System.out.println("Plugin destroyed: " + context.getPluginId());
        }
    }
}