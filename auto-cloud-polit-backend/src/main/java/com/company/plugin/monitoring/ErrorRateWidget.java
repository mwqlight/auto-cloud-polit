package com.company.plugin.monitoring;

import com.company.spi.DashboardWidget;

/**
 * 错误率组件
 */
public class ErrorRateWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "error-rate";
    }
    
    @Override
    public String getName() {
        return "错误率";
    }
    
    @Override
    public String getType() {
        return "bar";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public Object[] data = {
                new Object() { 
                    public String name = "HTTP 5xx错误"; 
                    public int[] values = {12, 15, 8, 10, 18, 22, 16};
                },
                new Object() { 
                    public String name = "HTTP 4xx错误"; 
                    public int[] values = {25, 18, 22, 19, 28, 32, 26};
                },
                new Object() { 
                    public String name = "业务错误"; 
                    public int[] values = {5, 8, 6, 7, 9, 12, 8};
                }
            };
            public String[] xAxis = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        };
    }
}