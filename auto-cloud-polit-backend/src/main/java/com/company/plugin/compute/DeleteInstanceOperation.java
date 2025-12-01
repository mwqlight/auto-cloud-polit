package com.company.plugin.compute;

import com.company.spi.Operation;

/**
 * 删除实例操作
 */
public class DeleteInstanceOperation implements Operation {
    
    @Override
    public String getId() {
        return "delete";
    }
    
    @Override
    public String getName() {
        return "删除实例";
    }
    
    @Override
    public String getDescription() {
        return "删除虚拟机实例";
    }
}