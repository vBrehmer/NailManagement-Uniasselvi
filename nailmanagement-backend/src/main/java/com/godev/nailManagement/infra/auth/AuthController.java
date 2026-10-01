package com.godev.nailManagement.infra.auth;

import com.godev.nailManagement.infra.common.dto.AuthRequest;
import com.godev.nailManagement.infra.common.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @GetMapping("/user")
    public String getWindowsUser() {
        return service.getWindowsUser();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/login")
    public AuthResponse auth(@RequestBody AuthRequest request) throws Exception{
        return service.auth(request);
    }

}
