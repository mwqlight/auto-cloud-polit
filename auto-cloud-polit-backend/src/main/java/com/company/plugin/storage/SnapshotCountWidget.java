package com.company.plugin.storage;

import com.company.spi.DashboardWidget;

/**
 * 快照数量组件
 */
public class SnapshotCountWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "snapshot-count";
    }
    
    @Override
    public String getName() {
        return "快照数量";
    }
    
    @Override
    public String getType() {
        return "counter";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public int count = 89;
            public String unit = "个";
            public String trend = "down";
        };
    }
}