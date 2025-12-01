package com.company.service;

import com.company.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * RBAC权限服务
 */
@Service
public class RbacService {
    
    // 角色权限映射
    private static final Map<Role, List<String>> ROLE_PERMISSIONS = new HashMap<>();
    
    static {
        // 管理员权限
        ROLE_PERMISSIONS.put(Role.ADMIN, Arrays.asList(
            "plugin.execute",
            "plugin.manage",
            "user.manage",
            "system.configure"
        ));
        
        // 用户权限
        ROLE_PERMISSIONS.put(Role.USER, Arrays.asList(
            "plugin.view",
            "dashboard.view"
        ));
        
        // 访客权限
        ROLE_PERMISSIONS.put(Role.GUEST, Arrays.asList(
            "public.view"
        ));
    }
    
    /**
     * 检查角色是否具有指定权限
     * @param role 角色
     * @param permission 权限
     * @return 是否具有权限
     */
    public boolean hasPermission(Role role, String permission) {
        List<String> permissions = ROLE_PERMISSIONS.get(role);
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * 获取角色的所有权限
     * @param role 角色
     * @return 权限列表
     */
    public List<String> getPermissions(Role role) {
        return ROLE_PERMISSIONS.getOrDefault(role, Arrays.asList());
    }
}