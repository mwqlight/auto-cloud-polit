package com.company.plugin.storage;

import com.company.spi.Operation;

/**
 * 卸载存储卷操作
 */
public class DetachVolumeOperation implements Operation {
    
    @Override
    public String getId() {
        return "detach-volume";
    }
    
    @Override
    public String getName() {
        return "卸载存储卷";
    }
    
    @Override
    public String getDescription() {
        return "从实例卸载存储卷";
    }
}