package com.company.plugin.security;

import com.company.spi.Operation;

/**
 * 创建WAF规则操作
 */
public class CreateWafRuleOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-waf-rule";
    }
    
    @Override
    public String getName() {
        return "创建WAF规则";
    }
    
    @Override
    public String getDescription() {
        return "创建Web应用防火墙规则";
    }
}