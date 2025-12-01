package com.company.plugin.security;

import com.company.spi.Operation;

/**
 * 创建防火墙规则操作
 */
public class CreateFirewallRuleOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-firewall-rule";
    }
    
    @Override
    public String getName() {
        return "创建防火墙规则";
    }
    
    @Override
    public String getDescription() {
        return "创建网络安全防火墙规则";
    }
}