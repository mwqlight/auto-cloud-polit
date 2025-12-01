package com.company.plugin.storage;

import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 存储插件生命周期管理
 */
public class StoragePluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private StorageComponent storageComponent;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.storageComponent = new StorageComponent();
        System.out.println("Storage plugin initialized");
    }
    
    @Override
    public void start() {
        // 注册存储组件
        context.registerComponent(storageComponent);
        System.out.println("Storage plugin started");
    }
    
    @Override
    public void stop() {
        System.out.println("Storage plugin stopped");
    }
    
    @Override
    public void destroy() {
        System.out.println("Storage plugin destroyed");
    }
}