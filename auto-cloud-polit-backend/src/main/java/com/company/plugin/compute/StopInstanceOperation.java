package com.company.plugin.compute;

import com.company.spi.Operation;

/**
 * 停止实例操作
 */
public class StopInstanceOperation implements Operation {
    
    @Override
    public String getId() {
        return "stop";
    }
    
    @Override
    public String getName() {
        return "停止实例";
    }
    
    @Override
    public String getDescription() {
        return "停止虚拟机实例";
    }
}