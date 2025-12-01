package com.company.plugin.monitoring;

import com.company.spi.DashboardWidget;

/**
 * 系统负载组件
 */
public class SystemLoadWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "system-load";
    }
    
    @Override
    public String getName() {
        return "系统负载";
    }
    
    @Override
    public String getType() {
        return "line";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public Object[] data = {
                new Object() { 
                    public String name = "CPU负载"; 
                    public double[] values = {0.6, 0.7, 0.8, 0.9, 0.7, 0.8, 0.9, 1.0, 0.9, 0.8};
                },
                new Object() { 
                    public String name = "内存负载"; 
                    public double[] values = {0.4, 0.5, 0.6, 0.7, 0.6, 0.7, 0.8, 0.9, 0.8, 0.7};
                },
                new Object() { 
                    public String name = "磁盘负载"; 
                    public double[] values = {0.2, 0.3, 0.4, 0.5, 0.4, 0.5, 0.6, 0.7, 0.6, 0.5};
                }
            };
            public String[] xAxis = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        };
    }
}