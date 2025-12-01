package com.company.plugin.network;

import com.company.spi.Operation;

/**
 * 创建负载均衡器操作
 */
public class CreateLoadBalancerOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-load-balancer";
    }
    
    @Override
    public String getName() {
        return "创建负载均衡器";
    }
    
    @Override
    public String getDescription() {
        return "创建负载均衡器";
    }
}