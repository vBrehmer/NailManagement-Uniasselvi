package com.godev.nailManagement.infra.common.dto;


public record AuthRequest(
        String login,
        String password
) {}
