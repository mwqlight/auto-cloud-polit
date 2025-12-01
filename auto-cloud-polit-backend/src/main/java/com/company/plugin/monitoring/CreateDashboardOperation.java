package com.company.plugin.monitoring;

import com.company.spi.Operation;

/**
 * 创建仪表盘操作
 */
public class CreateDashboardOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-dashboard";
    }
    
    @Override
    public String getName() {
        return "创建仪表盘";
    }
    
    @Override
    public String getDescription() {
        return "创建监控仪表盘";
    }
}