package com.company.plugin.storage;

import com.company.spi.CloudComponent;
import com.company.spi.ComponentType;
import com.company.spi.DashboardWidget;
import com.company.spi.Operation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 存储资源组件实现
 */
public class StorageComponent implements CloudComponent {
    
    @Override
    public String getId() {
        return "storage";
    }
    
    @Override
    public String getName() {
        return "存储资源";
    }
    
    @Override
    public ComponentType getType() {
        return ComponentType.STORAGE;
    }
    
    @Override
    public List<Operation> getSupportedOperations() {
        return Arrays.asList(
            new CreateVolumeOperation(),
            new AttachVolumeOperation(),
            new DetachVolumeOperation(),
            new DeleteVolumeOperation(),
            new CreateSnapshotOperation()
        );
    }
    
    @Override
    public Object execute(String operationId, Map<String, Object> params) {
        switch (operationId) {
            case "create-volume":
                return createVolume(params);
            case "attach-volume":
                return attachVolume(params);
            case "detach-volume":
                return detachVolume(params);
            case "delete-volume":
                return deleteVolume(params);
            case "create-snapshot":
                return createSnapshot(params);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operationId);
        }
    }
    
    @Override
    public DashboardWidget[] getDashboardWidgets() {
        return new DashboardWidget[] {
            new StorageUsageWidget(),
            new VolumeCountWidget(),
            new SnapshotCountWidget()
        };
    }
    
    // 操作实现方法
    private Object createVolume(Map<String, Object> params) {
        // 实现创建存储卷逻辑
        return Map.of("volumeId", "vol-" + System.currentTimeMillis(), "status", "creating");
    }
    
    private Object attachVolume(Map<String, Object> params) {
        // 实现挂载存储卷逻辑
        return Map.of("volumeId", params.get("volumeId"), "status", "attaching");
    }
    
    private Object detachVolume(Map<String, Object> params) {
        // 实现卸载存储卷逻辑
        return Map.of("volumeId", params.get("volumeId"), "status", "detaching");
    }
    
    private Object deleteVolume(Map<String, Object> params) {
        // 实现删除存储卷逻辑
        return Map.of("volumeId", params.get("volumeId"), "status", "deleting");
    }
    
    private Object createSnapshot(Map<String, Object> params) {
        // 实现创建快照逻辑
        return Map.of("snapshotId", "snap-" + System.currentTimeMillis(), "status", "creating");
    }
}