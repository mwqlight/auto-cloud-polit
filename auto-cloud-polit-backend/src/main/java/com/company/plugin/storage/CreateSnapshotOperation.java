package com.company.plugin.storage;

import com.company.spi.Operation;

/**
 * 创建快照操作
 */
public class CreateSnapshotOperation implements Operation {
    
    @Override
    public String getId() {
        return "create-snapshot";
    }
    
    @Override
    public String getName() {
        return "创建快照";
    }
    
    @Override
    public String getDescription() {
        return "为存储卷创建快照";
    }
}