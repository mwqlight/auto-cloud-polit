package com.company.config;

import com.company.entity.Role;
import com.company.entity.User;
import com.company.repository.UserRepository;
import com.company.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已经存在用户，如果不存在则创建默认用户
        if (userRepository.count() == 0) {
            // 创建管理员用户
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.ADMIN);
            
            userService.createUser("admin", "admin123", adminRoles);
            
            // 创建普通用户
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(Role.USER);
            
            userService.createUser("user", "user123", userRoles);
            
            System.out.println("默认用户已创建:");
            System.out.println("管理员账户: admin / admin123");
            System.out.println("普通用户账户: user / user123");
        }
    }
}