package com.company.plugin.compute;

import com.company.spi.DashboardWidget;

/**
 * 内存使用率组件
 */
public class MemoryUsageWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "memory-usage";
    }
    
    @Override
    public String getName() {
        return "内存使用率";
    }
    
    @Override
    public String getType() {
        return "chart";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public String chartType = "bar";
            public String[] data = {"70", "75", "80", "78", "82", "85", "83"};
            public String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        };
    }
}