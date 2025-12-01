package com.company.spi;

/**
 * 驾驶舱组件接口
 */
public interface DashboardWidget {
    
    /**
     * 获取组件ID
     */
    String getId();
    
    /**
     * 获取组件名称
     */
    String getName();
    
    /**
     * 获取组件类型
     */
    String getType();
    
    /**
     * 获取组件配置
     */
    Object getConfig();
}