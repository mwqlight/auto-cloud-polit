package com.company.plugin.security;

import com.company.spi.DashboardWidget;

/**
 * 攻击趋势组件
 */
public class AttackTrendWidget implements DashboardWidget {
    
    @Override
    public String getId() {
        return "attack-trend";
    }
    
    @Override
    public String getName() {
        return "攻击趋势";
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
                    public String name = "DDoS攻击"; 
                    public int[] values = {12, 15, 8, 10, 18, 22, 16};
                },
                new Object() { 
                    public String name = "Web攻击"; 
                    public int[] values = {25, 18, 22, 19, 28, 32, 26};
                },
                new Object() { 
                    public String name = "恶意扫描"; 
                    public int[] values = {35, 42, 38, 45, 52, 48, 55};
                }
            };
            public String[] xAxis = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        };
    }
}