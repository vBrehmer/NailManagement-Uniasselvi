package com.godev.nailManagement.common.dtos;

import java.util.UUID;

public record AgentResponse(
        UUID id,
        String name,
        String area
) {}
