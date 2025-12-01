package com.company.plugin.storage;

import com.company.spi.DashboardWidget;

/**
 * 存储使用率组件
 */
public class StorageUsageWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "storage-usage";
    }
    
    @Override
    public String getName() {
        return "存储使用率";
    }
    
    @Override
    public String getType() {
        return "pie";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public Object[] data = {
                new Object() { public String name = "已使用"; public double value = 65.5; },
                new Object() { public String name = "未使用"; public double value = 34.5; }
            };
            public String unit = "%";
        };
    }
}