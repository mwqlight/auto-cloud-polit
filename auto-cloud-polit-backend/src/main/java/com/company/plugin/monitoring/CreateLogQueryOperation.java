package com.company.plugin.monitoring;

import com.company.spi.Operation;

/**
 * 创建日志查询操作
 */
public class CreateLogQueryOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-log-query";
    }
    
    @Override
    public String getName() {
        return "创建日志查询";
    }
    
    @Override
    public String getDescription() {
        return "创建日志查询语句";
    }
}