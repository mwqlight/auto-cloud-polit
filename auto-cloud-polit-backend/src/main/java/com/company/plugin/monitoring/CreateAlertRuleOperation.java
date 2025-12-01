package com.company.plugin.monitoring;

import com.company.spi.Operation;

/**
 * 创建告警规则操作
 */
public class CreateAlertRuleOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-alert-rule";
    }
    
    @Override
    public String getName() {
        return "创建告警规则";
    }
    
    @Override
    public String getDescription() {
        return "创建监控告警规则";
    }
}