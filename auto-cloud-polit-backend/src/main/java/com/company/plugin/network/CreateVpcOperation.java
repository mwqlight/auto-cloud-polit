package com.company.plugin.network;

import com.company.spi.Operation;

/**
 * 创建VPC操作
 */
public class CreateVpcOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-vpc";
    }
    
    @Override
    public String getName() {
        return "创建VPC";
    }
    
    @Override
    public String getDescription() {
        return "创建虚拟私有云";
    }
}