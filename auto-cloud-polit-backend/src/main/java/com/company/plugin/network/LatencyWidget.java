package com.company.plugin.network;

import com.company.spi.DashboardWidget;

/**
 * 网络延迟组件
 */
public class LatencyWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "network-latency";
    }
    
    @Override
    public String getName() {
        return "网络延迟";
    }
    
    @Override
    public String getType() {
        return "gauge";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public double value = 28.5;
            public String unit = "ms";
            public double min = 0;
            public double max = 100;
            public Object[] thresholds = {
                new Object() { public double value = 30; public String color = "#52c41a"; },
                new Object() { public double value = 70; public String color = "#faad14"; },
                new Object() { public double value = 100; public String color = "#ff4d4f"; }
            };
        };
    }
}