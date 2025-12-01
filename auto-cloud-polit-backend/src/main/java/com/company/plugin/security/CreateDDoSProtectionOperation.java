package com.company.plugin.security;

import com.company.spi.Operation;

/**
 * 创建DDoS防护操作
 */
public class CreateDDoSProtectionOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-ddos-protection";
    }
    
    @Override
    public String getName() {
        return "创建DDoS防护";
    }
    
    @Override
    public String getDescription() {
        return "启用DDoS攻击防护";
    }
}