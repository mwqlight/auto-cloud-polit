package com.company.spi;

/**
 * 操作接口
 */
public interface Operation {
    
    /**
     * 获取操作ID
     */
    String getId();
    
    /**
     * 获取操作名称
     */
    String getName();
    
    /**
     * 获取操作描述
     */
    String getDescription();
}