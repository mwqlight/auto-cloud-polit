package com.company.controller;

import com.company.plugin.PluginManager;
import com.company.service.PluginService;
import com.company.spi.CloudComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 插件测试控制器
 * 用于验证插件系统功能
 */
@RestController
@RequestMapping("/api/plugins/test")
public class PluginTestController {
    
    @Autowired
    private PluginService pluginService;
    
    /**
     * 获取所有插件组件信息
     */
    @GetMapping("/components")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<String, Object> getAllComponents() {
        Map<String, Object> result = new HashMap<>();
        
        PluginManager pluginManager = pluginService.getPluginManager();
        Collection<CloudComponent> components = pluginManager.getComponents();
        
        result.put("count", components.size());
        result.put("components", components);
        
        return result;
    }
}