package com.company.plugin.compute;

import com.company.spi.Operation;

/**
 * 创建实例操作
 */
public class CreateInstanceOperation implements Operation {
    
    @Override
    public String getId() {
        return "create";
    }
    
    @Override
    public String getName() {
        return "创建实例";
    }
    
    @Override
    public String getDescription() {
        return "创建新的虚拟机实例";
    }
}