package com.company.spi;

import java.util.List;
import java.util.Map;

/**
 * 云组件SPI接口
 */
public interface CloudComponent {
    
    /**
     * 获取组件唯一标识
     */
    String getId();
    
    /**
     * 获取组件显示名称
     */
    String getName();
    
    /**
     * 获取组件类型
     */
    ComponentType getType();
    
    /**
     * 获取支持的操作列表
     */
    List<Operation> getSupportedOperations();
    
    /**
     * 执行操作
     */
    Object execute(String operationId, Map<String, Object> params);
    
    /**
     * 获取驾驶舱组件
     */
    DashboardWidget[] getDashboardWidgets();
}