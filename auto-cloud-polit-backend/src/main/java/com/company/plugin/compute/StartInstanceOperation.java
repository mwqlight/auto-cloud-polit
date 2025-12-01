package com.company.plugin.compute;

import com.company.spi.Operation;

/**
 * 启动实例操作
 */
public class StartInstanceOperation implements Operation {
    
    @Override
    public String getId() {
        return "start";
    }
    
    @Override
    public String getName() {
        return "启动实例";
    }
    
    @Override
    public String getDescription() {
        return "启动虚拟机实例";
    }
}