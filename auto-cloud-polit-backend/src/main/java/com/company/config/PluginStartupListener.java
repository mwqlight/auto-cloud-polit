package com.company.config;

import com.company.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 插件启动监听器
 * 在Spring应用上下文刷新完成后启动插件
 */
@Component
public class PluginStartupListener implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private PluginService pluginService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 确保只在根应用上下文启动时执行一次
        if (event.getApplicationContext().getParent() == null) {
            System.out.println("Starting plugins...");
            pluginService.startPlugins();
        }
    }
}