package com.company.plugin.compute;

import com.company.spi.DashboardWidget;

/**
 * 实例数量组件
 */
public class InstanceCountWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "instance-count";
    }
    
    @Override
    public String getName() {
        return "实例数量";
    }
    
    @Override
    public String getType() {
        return "counter";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public int count = 24;
            public String unit = "台";
            public String trend = "up";
        };
    }
}