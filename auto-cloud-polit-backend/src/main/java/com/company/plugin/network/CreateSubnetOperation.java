package com.company.plugin.network;

import com.company.spi.Operation;

/**
 * 创建子网操作
 */
public class CreateSubnetOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-subnet";
    }
    
    @Override
    public String getName() {
        return "创建子网";
    }
    
    @Override
    public String getDescription() {
        return "在VPC中创建子网";
    }
}