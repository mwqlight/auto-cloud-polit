package com.company.plugin.compute;

import com.company.spi.CloudComponent;
import com.company.spi.ComponentType;
import com.company.spi.DashboardWidget;
import com.company.spi.Operation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 计算资源组件实现
 */
public class ComputeComponent implements CloudComponent {
    
    @Override
    public String getId() {
        return "compute";
    }
    
    @Override
    public String getName() {
        return "计算资源";
    }
    
    @Override
    public ComponentType getType() {
        return ComponentType.COMPUTE;
    }
    
    @Override
    public List<Operation> getSupportedOperations() {
        return Arrays.asList(
            new CreateInstanceOperation(),
            new StartInstanceOperation(),
            new StopInstanceOperation(),
            new ResizeInstanceOperation(),
            new DeleteInstanceOperation()
        );
    }
    
    @Override
    public Object execute(String operationId, Map<String, Object> params) {
        switch (operationId) {
            case "create":
                return createInstance(params);
            case "start":
                return startInstance(params);
            case "stop":
                return stopInstance(params);
            case "resize":
                return resizeInstance(params);
            case "delete":
                return deleteInstance(params);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operationId);
        }
    }
    
    @Override
    public DashboardWidget[] getDashboardWidgets() {
        return new DashboardWidget[] {
            new CpuUsageWidget(),
            new MemoryUsageWidget(),
            new InstanceCountWidget()
        };
    }
    
    // 操作实现方法
    private Object createInstance(Map<String, Object> params) {
        // 实现创建实例逻辑
        return Map.of("instanceId", "i-" + System.currentTimeMillis(), "status", "creating");
    }
    
    private Object startInstance(Map<String, Object> params) {
        // 实现启动实例逻辑
        return Map.of("instanceId", params.get("instanceId"), "status", "starting");
    }
    
    private Object stopInstance(Map<String, Object> params) {
        // 实现停止实例逻辑
        return Map.of("instanceId", params.get("instanceId"), "status", "stopping");
    }
    
    private Object resizeInstance(Map<String, Object> params) {
        // 实现调整实例规格逻辑
        return Map.of("instanceId", params.get("instanceId"), "status", "resizing");
    }
    
    private Object deleteInstance(Map<String, Object> params) {
        // 实现删除实例逻辑
        return Map.of("instanceId", params.get("instanceId"), "status", "deleting");
    }
}