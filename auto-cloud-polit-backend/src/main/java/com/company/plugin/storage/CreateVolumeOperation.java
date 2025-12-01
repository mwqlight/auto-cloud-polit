package com.company.plugin.storage;

import com.company.spi.Operation;

/**
 * 创建存储卷操作
 */
public class CreateVolumeOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-volume";
    }
    
    @Override
    public String getName() {
        return "创建存储卷";
    }
    
    @Override
    public String getDescription() {
        return "创建新的存储卷";
    }
}