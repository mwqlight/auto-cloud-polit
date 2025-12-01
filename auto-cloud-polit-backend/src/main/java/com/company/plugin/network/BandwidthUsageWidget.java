package com.company.plugin.network;

import com.company.spi.DashboardWidget;

/**
 * 带宽使用率组件
 */
public class BandwidthUsageWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "bandwidth-usage";
    }
    
    @Override
    public String getName() {
        return "带宽使用率";
    }
    
    @Override
    public String getType() {
        return "area";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public Object[] data = {
                new Object() { 
                    public String name = "入站流量"; 
                    public double[] values = {120, 132, 101, 134, 90, 230, 210, 150, 180, 200};
                },
                new Object() { 
                    public String name = "出站流量"; 
                    public double[] values = {220, 182, 191, 234, 290, 330, 310, 280, 290, 310};
                }
            };
            public String[] xAxis = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        };
    }
}