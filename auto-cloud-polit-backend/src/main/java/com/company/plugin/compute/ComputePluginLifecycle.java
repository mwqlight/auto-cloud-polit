package com.company.plugin.compute;

import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 计算插件生命周期管理
 */
public class ComputePluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private ComputeComponent computeComponent;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.computeComponent = new ComputeComponent();
        System.out.println("Compute plugin initialized");
    }
    
    @Override
    public void start() {
        // 注册计算组件
        context.registerComponent(computeComponent);
        System.out.println("Compute plugin started");
    }
    
    @Override
    public void stop() {
        System.out.println("Compute plugin stopped");
    }
    
    @Override
    public void destroy() {
        System.out.println("Compute plugin destroyed");
    }
}