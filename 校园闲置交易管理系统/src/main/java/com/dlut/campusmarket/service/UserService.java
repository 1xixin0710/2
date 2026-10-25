package com.dlut.campusmarket.service;

import com.dlut.campusmarket.mapper.UserMapper;
import com.dlut.campusmarket.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String username, String password) {
        User user = userMapper.findByUsername(username == null ? "" : username.trim());
        if (user == null || !passwordEncoder.matches(password == null ? "" : password, user.getPasswordHash())) {
            return null;
        }
        user.setPassword(null);
        user.setPasswordHash(null);
        return user;
    }

    @Transactional
    public boolean register(User user) {
        String username = user.getUsername().trim();
        if (userMapper.findByUsername(username) != null) {
            return false;
        }
        user.setUsername(username);
        user.setDisplayName(user.getDisplayName().trim());
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        user.setPassword(null);
        userMapper.insert(user);
        return true;
    }
}
