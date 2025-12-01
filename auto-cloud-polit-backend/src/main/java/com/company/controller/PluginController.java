package com.company.controller;

import com.company.config.PermissionChecker;
import com.company.plugin.PluginManager;
import com.company.service.PluginService;
import com.company.spi.CloudComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 插件管理控制器
 */
@RestController
@RequestMapping("/api/v1/plugins")
public class PluginController {
    
    @Autowired
    private PluginService pluginService;
    
    /**
     * 获取所有插件组件
     */
    @GetMapping("/components")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Map<String, Object>> getAllComponents() {
        PluginManager pluginManager = pluginService.getPluginManager();
        Collection<CloudComponent> components = pluginManager.getComponents();
        
        List<Map<String, Object>> result = new ArrayList<>();
        components.forEach(component -> {
            result.add(Map.of(
                "id", component.getId(),
                "name", component.getName(),
                "type", component.getType().name()
            ));
        });
        
        return result;
    }
    
    /**
     * 获取指定组件支持的操作
     */
    @GetMapping("/components/{componentId}/operations")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Map<String, Object>> getComponentOperations(@PathVariable String componentId) {
        PluginManager pluginManager = pluginService.getPluginManager();
        CloudComponent component = pluginManager.getComponent(componentId);
        
        if (component == null) {
            throw new RuntimeException("Component not found: " + componentId);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        component.getSupportedOperations().forEach(operation -> {
            result.add(Map.of(
                "id", operation.getId(),
                "name", operation.getName(),
                "description", operation.getDescription()
            ));
        });
        
        return result;
    }
    
    /**
     * 执行组件操作
     */
    @PostMapping("/components/{componentId}/execute")
    @PreAuthorize("hasRole('ADMIN')")
    public Object executeOperation(
            @PathVariable String componentId,
            @RequestParam String operationId,
            @RequestBody Map<String, Object> params) {
        PluginManager pluginManager = pluginService.getPluginManager();
        CloudComponent component = pluginManager.getComponent(componentId);
        
        if (component == null) {
            throw new RuntimeException("Component not found: " + componentId);
        }
        
        return component.execute(operationId, params);
    }
    
    /**
     * 获取组件仪表盘部件
     */
    @GetMapping("/components/{componentId}/widgets")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Map<String, Object>> getComponentWidgets(@PathVariable String componentId) {
        PluginManager pluginManager = pluginService.getPluginManager();
        CloudComponent component = pluginManager.getComponent(componentId);
        
        if (component == null) {
            throw new RuntimeException("Component not found: " + componentId);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.company.spi.DashboardWidget widget : component.getDashboardWidgets()) {
            result.add(Map.of(
                "id", widget.getId(),
                "name", widget.getName(),
                "type", widget.getType(),
                "config", widget.getConfig()
            ));
        }
        
        return result;
    }
}