package com.company.plugin.compute;

import com.company.spi.Operation;

/**
 * 调整实例规格操作
 */
public class ResizeInstanceOperation implements Operation {
    
    @Override
    public String getId() {
        return "resize";
    }
    
    @Override
    public String getName() {
        return "调整规格";
    }
    
    @Override
    public String getDescription() {
        return "调整虚拟机实例的CPU和内存配置";
    }
}