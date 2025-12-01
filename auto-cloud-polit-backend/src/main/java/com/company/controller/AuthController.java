package com.company.controller;

import com.company.dto.response.ApiResponse;
import com.company.entity.Role;
import com.company.entity.User;
import com.company.service.UserService;
import com.company.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 查询用户
            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 验证密码
            if (!userService.validatePassword(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(new ApiResponse(401, "用户名或密码错误", null));
            }

            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtil.generateToken(loginRequest.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", jwt);
            data.put("username", loginRequest.getUsername());
            
            return ResponseEntity.ok(new ApiResponse(200, "登录成功", data));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "登录失败: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // 设置默认角色为USER
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            
            // 创建用户
            User user = userService.createUser(registerRequest.getUsername(), registerRequest.getPassword(), roles);
            
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("id", user.getId());
            
            return ResponseEntity.ok(new ApiResponse(200, "注册成功", data));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "注册失败: " + e.getMessage(), null));
        }
    }

    // 登录请求数据传输对象
    static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // 注册请求数据传输对象
    static class RegisterRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}