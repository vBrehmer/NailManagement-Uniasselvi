package com.godev.nailManagement.infra.auth;

import com.godev.nailManagement.common.enums.Role;
import com.godev.nailManagement.common.exceptions.BusinessException;
import com.godev.nailManagement.domain.client.Client;
import com.godev.nailManagement.infra.common.dto.AuthRequest;
import com.godev.nailManagement.infra.common.dto.AuthResponse;
import com.godev.nailManagement.domain.client.ClientRepository;
import com.godev.nailManagement.infra.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse auth(AuthRequest request) {
        Client user = clientRepository.findByUsername(request.login())
                .orElseThrow(() -> new IllegalStateException("User not found at AuthService!"));

        if (user.getRole() !=  Role.ADMIN) {
            throw new BusinessException("Insuficient permission!");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }

    public String getWindowsUser() {
        return System.getenv("USERNAME");
    }
}
