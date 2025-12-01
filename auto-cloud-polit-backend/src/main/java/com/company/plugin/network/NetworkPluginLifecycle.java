package com.company.plugin.network;

import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 网络插件生命周期管理
 */
public class NetworkPluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private NetworkComponent networkComponent;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.networkComponent = new NetworkComponent();
        System.out.println("Network plugin initialized");
    }
    
    @Override
    public void start() {
        // 注册网络组件
        context.registerComponent(networkComponent);
        System.out.println("Network plugin started");
    }
    
    @Override
    public void stop() {
        System.out.println("Network plugin stopped");
    }
    
    @Override
    public void destroy() {
        System.out.println("Network plugin destroyed");
    }
}