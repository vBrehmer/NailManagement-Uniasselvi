package com.godev.nailManagement.common.dtos;

public record PasswordRequest(
        String old_password,
        String new_password
) {}
