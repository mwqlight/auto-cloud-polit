package com.company.plugin.security;

import com.company.spi.DashboardWidget;

/**
 * 威胁统计组件
 */
public class ThreatCountWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "threat-count";
    }
    
    @Override
    public String getName() {
        return "威胁统计";
    }
    
    @Override
    public String getType() {
        return "counter";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public int count = 24;
            public String unit = "个";
            public String trend = "down";
        };
    }
}