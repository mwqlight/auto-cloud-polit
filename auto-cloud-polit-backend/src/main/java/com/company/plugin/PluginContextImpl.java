package com.company.plugin;

import com.company.spi.CloudComponent;
import com.company.spi.PluginContext;

/**
 * 插件上下文实现类
 */
public class PluginContextImpl implements PluginContext {
    
    private final String pluginId;
    private final PluginManager pluginManager;
    private final Object configuration;
    
    public PluginContextImpl(String pluginId, PluginManager pluginManager) {
        this.pluginId = pluginId;
        this.pluginManager = pluginManager;
        this.configuration = null;
    }
    
    @Override
    public String getPluginId() {
        return pluginId;
    }
    
    @Override
    public Object getConfiguration() {
        return configuration;
    }
    
    @Override
    public void registerComponent(CloudComponent component) {
        pluginManager.registerComponent(pluginId, component);
    }
    
    @Override
    public CloudComponent getComponent(String componentId) {
        return pluginManager.getComponent(componentId);
    }
}