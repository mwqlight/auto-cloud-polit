package com.company.config;

import com.company.entity.Role;
import com.company.entity.User;
import com.company.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * 自定义权限评估器
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    
    @Autowired
    private RbacService rbacService;
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // 获取当前用户的角色
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            Set<Role> roles = user.getRoles();
            
            // 检查角色是否具有指定权限
            for (Role role : roles) {
                if (rbacService.hasPermission(role, permission.toString())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetType, permission);
    }
}