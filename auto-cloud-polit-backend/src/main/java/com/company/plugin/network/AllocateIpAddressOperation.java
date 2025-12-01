package com.company.plugin.network;

import com.company.spi.Operation;

/**
 * 分配IP地址操作
 */
public class AllocateIpAddressOperation implements Operation {
    
    @Override
    public String getId() {
        return "allocate-ip-address";
    }
    
    @Override
    public String getName() {
        return "分配IP地址";
    }
    
    @Override
    public String getDescription() {
        return "分配公网IP地址";
    }
}