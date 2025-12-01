package com.company.plugin.storage;

import com.company.spi.Operation;

/**
 * 挂载存储卷操作
 */
public class AttachVolumeOperation implements Operation {
    
    @Override
    public String getId() {
        return "attach-volume";
    }
    
    @Override
    public String getName() {
        return "挂载存储卷";
    }
    
    @Override
    public String getDescription() {
        return "将存储卷挂载到实例";
    }
}