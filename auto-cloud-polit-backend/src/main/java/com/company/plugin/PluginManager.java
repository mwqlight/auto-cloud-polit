package com.company.plugin;

import com.company.spi.CloudComponent;
import com.company.spi.PluginContext;
import com.company.spi.PluginLifecycle;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

/**
 * 插件管理器
 */
@Component
public class PluginManager {
    
    // 插件映射
    private final Map<String, PluginInfo> plugins = new ConcurrentHashMap<>();
    
    // 组件映射
    private final Map<String, CloudComponent> components = new ConcurrentHashMap<>();
    
    // 插件目录
    private static final String PLUGIN_DIR = "plugins";
    
    /**
     * 加载所有插件
     */
    public void loadPlugins() {
        File pluginDir = new File(PLUGIN_DIR);
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
            return;
        }
        
        File[] pluginFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (pluginFiles == null) return;
        
        for (File pluginFile : pluginFiles) {
            try {
                loadPlugin(pluginFile);
            } catch (Exception e) {
                System.err.println("Failed to load plugin: " + pluginFile.getName() + ", error: " + e.getMessage());
            }
        }
    }
    
    /**
     * 启动所有插件
     */
    public void startPlugins() {
        for (PluginInfo pluginInfo : plugins.values()) {
            try {
                PluginLifecycle pluginLifecycle = pluginInfo.getPluginLifecycle();
                if (pluginLifecycle != null) {
                    pluginLifecycle.start();
                }
            } catch (Exception e) {
                System.err.println("Failed to start plugin: " + pluginInfo.getId() + ", error: " + e.getMessage());
            }
        }
    }
    
    /**
     * 停止所有插件
     */
    public void stopPlugins() {
        for (PluginInfo pluginInfo : plugins.values()) {
            try {
                PluginLifecycle pluginLifecycle = pluginInfo.getPluginLifecycle();
                if (pluginLifecycle != null) {
                    pluginLifecycle.stop();
                }
            } catch (Exception e) {
                System.err.println("Failed to stop plugin: " + pluginInfo.getId() + ", error: " + e.getMessage());
            }
        }
    }
    
    /**
     * 销毁所有插件
     */
    public void destroyPlugins() {
        for (PluginInfo pluginInfo : plugins.values()) {
            try {
                PluginLifecycle pluginLifecycle = pluginInfo.getPluginLifecycle();
                if (pluginLifecycle != null) {
                    pluginLifecycle.destroy();
                }
            } catch (Exception e) {
                System.err.println("Failed to destroy plugin: " + pluginInfo.getId() + ", error: " + e.getMessage());
            }
        }
    }
    
    /**
     * 加载单个插件
     */
    private void loadPlugin(File pluginFile) throws Exception {
        URLClassLoader classLoader = new URLClassLoader(
            new URL[]{pluginFile.toURI().toURL()},
            this.getClass().getClassLoader()
        );
        
        // 读取插件配置文件
        Properties pluginProperties = PluginConfigLoader.loadConfigFromJar(pluginFile);
        
        String pluginId = pluginFile.getName().replace(".jar", "");
        PluginInfo pluginInfo = new PluginInfo(pluginId, pluginFile, classLoader);
        plugins.put(pluginId, pluginInfo);
        
        // 初始化插件
        initializePlugin(pluginInfo);
    }
    
    /**
     * 初始化插件
     */
    private void initializePlugin(PluginInfo pluginInfo) {
        // 创建插件上下文
        PluginContext context = new PluginContextImpl(pluginInfo.getId(), this);
        
        // 查找并实例化PluginLifecycle实现类
        try {
            // 尝试从插件配置文件中读取生命周期类名
            String lifecycleClassName = "com.company.plugin.lifecycle.DefaultPluginLifecycle";
            Class<?> pluginClass = pluginInfo.getClassLoader().loadClass(lifecycleClassName);
            PluginLifecycle pluginLifecycle = (PluginLifecycle) pluginClass.newInstance();
            
            pluginInfo.setPluginLifecycle(pluginLifecycle);
            
            // 初始化插件
            pluginLifecycle.init(context);
            
            System.out.println("Plugin initialized successfully: " + pluginInfo.getId());
        } catch (Exception e) {
            System.err.println("Failed to initialize plugin: " + pluginInfo.getId() + ", error: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有插件
     */
    public Collection<PluginInfo> getPlugins() {
        return plugins.values();
    }
    
    /**
     * 根据ID获取插件
     */
    public PluginInfo getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }
    
    /**
     * 注册组件
     */
    public void registerComponent(String pluginId, CloudComponent component) {
        synchronized (components) {
            components.put(component.getId(), component);
        }
        System.out.println("Component registered: " + component.getId() + " from plugin: " + pluginId);
    }
    
    /**
     * 获取所有组件
     */
    public Collection<CloudComponent> getComponents() {
        return components.values();
    }
    
    /**
     * 获取所有组件映射
     */
    public Map<String, CloudComponent> getAllComponents() {
        return components;
    }
    
    /**
     * 根据ID获取组件
     */
    public CloudComponent getComponent(String componentId) {
        return components.get(componentId);
    }
}