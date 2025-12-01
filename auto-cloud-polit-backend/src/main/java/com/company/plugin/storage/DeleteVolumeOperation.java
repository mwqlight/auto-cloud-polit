package com.company.plugin.storage;

import com.company.spi.Operation;

/**
 * 删除存储卷操作
 */
public class DeleteVolumeOperation implements Operation {
    
    @Override
    public String getId() {
        return "delete-volume";
    }
    
    @Override
    public String getName() {
        return "删除存储卷";
    }
    
    @Override
    public String getDescription() {
        return "删除指定的存储卷";
    }
}