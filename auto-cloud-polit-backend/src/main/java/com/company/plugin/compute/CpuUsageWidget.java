package com.company.plugin.compute;

import com.company.spi.DashboardWidget;

/**
 * CPU使用率组件
 */
public class CpuUsageWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "cpu-usage";
    }
    
    @Override
    public String getName() {
        return "CPU使用率";
    }
    
    @Override
    public String getType() {
        return "chart";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public String chartType = "line";
            public String[] data = {"60", "65", "70", "68", "72", "75", "73"};
            public String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        };
    }
}