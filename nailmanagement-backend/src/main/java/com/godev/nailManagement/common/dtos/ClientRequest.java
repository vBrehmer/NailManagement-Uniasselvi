package com.godev.nailManagement.common.dtos;

import com.godev.nailManagement.common.enums.Role;

public record ClientRequest(
        String username,
        String password,
        Role role,
        String email
){}
