package com.company.plugin.security;

import com.company.spi.Operation;

/**
 * 创建证书操作
 */
public class CreateCertificateOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-certificate";
    }
    
    @Override
    public String getName() {
        return "创建证书";
    }
    
    @Override
    public String getDescription() {
        return "申请和部署SSL证书";
    }
}