package com.company.spi;

/**
 * 插件上下文接口
 */
public interface PluginContext {
    
    /**
     * 获取插件ID
     */
    String getPluginId();
    
    /**
     * 获取插件配置
     */
    Object getConfiguration();
    
    /**
     * 注册组件
     */
    void registerComponent(CloudComponent component);
    
    /**
     * 获取组件
     */
    CloudComponent getComponent(String componentId);
}