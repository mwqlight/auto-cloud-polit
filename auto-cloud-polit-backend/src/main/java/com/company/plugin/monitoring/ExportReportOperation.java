package com.company.plugin.monitoring;

import com.company.spi.Operation;

/**
 * 导出报告操作
 */
public class ExportReportOperation implements Operation {
    
    @Override
    public String getId() {
        return "export-report";
    }
    
    @Override
    public String getName() {
        return "导出报告";
    }
    
    @Override
    public String getDescription() {
        return "导出监控报告";
    }
}