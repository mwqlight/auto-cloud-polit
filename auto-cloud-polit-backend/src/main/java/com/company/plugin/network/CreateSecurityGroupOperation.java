package com.company.plugin.network;

import com.company.spi.Operation;

/**
 * 创建安全组操作
 */
public class CreateSecurityGroupOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-security-group";
    }
    
    @Override
    public String getName() {
        return "创建安全组";
    }
    
    @Override
    public String getDescription() {
        return "创建网络安全组";
    }
}