package com.company.plugin.storage;

import com.company.spi.DashboardWidget;

/**
 * 存储卷数量组件
 */
public class VolumeCountWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "volume-count";
    }
    
    @Override
    public String getName() {
        return "存储卷数量";
    }
    
    @Override
    public String getType() {
        return "counter";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public int count = 156;
            public String unit = "个";
            public String trend = "up";
        };
    }
}