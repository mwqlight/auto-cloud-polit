package com.company.spi;

/**
 * 插件生命周期接口
 */
public interface PluginLifecycle {
    
    /**
     * 初始化插件
     */
    void init(PluginContext context);
    
    /**
     * 启动插件
     */
    void start();
    
    /**
     * 停止插件
     */
    void stop();
    
    /**
     * 销毁插件
     */
    void destroy();
}