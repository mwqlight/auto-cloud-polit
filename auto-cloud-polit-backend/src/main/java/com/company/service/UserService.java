package com.company.service;

import com.company.entity.User;
import com.company.entity.Role;
import com.company.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 创建新用户
     * @param username 用户名
     * @param password 密码
     * @param roles 角色集合
     * @return 创建的用户
     */
    public User createUser(String username, String password, Set<Role> roles) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(password);

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 验证用户密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}