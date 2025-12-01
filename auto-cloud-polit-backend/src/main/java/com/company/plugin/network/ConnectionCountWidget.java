package com.company.plugin.network;

import com.company.spi.DashboardWidget;

/**
 * 连接数统计组件
 */
public class ConnectionCountWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "connection-count";
    }
    
    @Override
    public String getName() {
        return "连接数统计";
    }
    
    @Override
    public String getType() {
        return "counter";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public int count = 1245;
            public String unit = "个";
            public String trend = "up";
        };
    }
}