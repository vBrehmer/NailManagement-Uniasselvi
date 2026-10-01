package com.godev.nailManagement.common.dtos;

import java.util.UUID;

public record ProcedureResponse(
        UUID id,
        String name,
        Float value
) {}
