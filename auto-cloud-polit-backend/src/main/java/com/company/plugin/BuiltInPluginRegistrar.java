package com.company.plugin;

import com.company.plugin.compute.ComputeComponent;
import com.company.plugin.monitoring.MonitoringComponent;
import com.company.plugin.network.NetworkComponent;
import com.company.plugin.security.SecurityComponent;
import com.company.plugin.storage.StorageComponent;
import com.company.spi.CloudComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 内置插件注册器
 * 用于注册系统内置的插件组件
 */
@Component
public class BuiltInPluginRegistrar {
    
    @Autowired
    private PluginManager pluginManager;
    
    // 内置插件组件列表
    private final List<CloudComponent> builtInComponents = Arrays.asList(
        new ComputeComponent(),
        new StorageComponent(),
        new NetworkComponent(),
        new SecurityComponent(),
        new MonitoringComponent()
    );
    
    /**
     * 注册内置插件组件
     */
    @PostConstruct
    public void registerBuiltInComponents() {
        for (CloudComponent component : builtInComponents) {
            pluginManager.registerComponent("builtin", component);
        }
        System.out.println("Built-in components registered: " + builtInComponents.size());
    }
}