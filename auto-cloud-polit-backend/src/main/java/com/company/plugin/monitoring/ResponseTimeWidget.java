package com.company.plugin.monitoring;

import com.company.spi.DashboardWidget;

/**
 * 响应时间组件
 */
public class ResponseTimeWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "response-time";
    }
    
    @Override
    public String getName() {
        return "响应时间";
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
                    public String name = "平均响应时间"; 
                    public double[] values = {120, 132, 101, 134, 90, 230, 210, 150, 180, 200};
                },
                new Object() { 
                    public String name = "最大响应时间"; 
                    public double[] values = {220, 182, 191, 234, 290, 330, 310, 280, 290, 310};
                }
            };
            public String[] xAxis = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        };
    }
}