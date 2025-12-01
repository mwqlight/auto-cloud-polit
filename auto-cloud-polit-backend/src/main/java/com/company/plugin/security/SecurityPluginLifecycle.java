package com.company.plugin.security;

import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;

/**
 * 安全插件生命周期管理
 */
public class SecurityPluginLifecycle implements PluginLifecycle {
    
    private PluginContext context;
    private SecurityComponent securityComponent;
    
    @Override
    public void init(PluginContext context) {
        this.context = context;
        this.securityComponent = new SecurityComponent();
        System.out.println("Security plugin initialized");
    }
    
    @Override
    public void start() {
        // 注册安全组件
        context.registerComponent(securityComponent);
        System.out.println("Security plugin started");
    }
    
    @Override
    public void stop() {
        System.out.println("Security plugin stopped");
    }
    
    @Override
    public void destroy() {
        System.out.println("Security plugin destroyed");
    }
}