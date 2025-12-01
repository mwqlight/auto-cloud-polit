package com.company.plugin.monitoring;

import com.company.spi.Operation;

/**
 * 创建指标操作
 */
public class CreateMetricOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-metric";
    }
    
    @Override
    public String getName() {
        return "创建指标";
    }
    
    @Override
    public String getDescription() {
        return "创建监控指标";
    }
}