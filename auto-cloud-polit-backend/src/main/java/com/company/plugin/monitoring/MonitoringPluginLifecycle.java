package com.company.plugin.monitoring;

import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 监控插件生命周期管理
 */
public class MonitoringPluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private MonitoringComponent monitoringComponent;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.monitoringComponent = new MonitoringComponent();
        System.out.println("Monitoring plugin initialized");
    }
    
    @Override
    public void start() {
        // 注册监控组件
        context.registerComponent(monitoringComponent);
        System.out.println("Monitoring plugin started");
    }
    
    @Override
    public void stop() {
        System.out.println("Monitoring plugin stopped");
    }
    
    @Override
    public void destroy() {
        System.out.println("Monitoring plugin destroyed");
    }
}