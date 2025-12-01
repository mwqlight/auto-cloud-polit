package com.company.plugin.security;

import com.company.spi.DashboardWidget;

/**
 * 安全评分组件
 */
public class SecurityScoreWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "security-score";
    }
    
    @Override
    public String getName() {
        return "安全评分";
    }
    
    @Override
    public String getType() {
        return "gauge";
    }
    
    @Override
    public Object getConfig() {
        return new Object() {
            public double value = 85.5;
            public String unit = "分";
            public double min = 0;
            public double max = 100;
            public Object[] thresholds = {
                new Object() { public double value = 60; public String color = "#ff4d4f"; },
                new Object() { public double value = 80; public String color = "#faad14"; },
                new Object() { public double value = 100; public String color = "#52c41a"; }
            };
        };
    }
}