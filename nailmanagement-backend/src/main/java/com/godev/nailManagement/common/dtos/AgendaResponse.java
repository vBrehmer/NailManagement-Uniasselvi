package com.godev.nailManagement.common.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AgendaResponse(
        UUID id,
        String client_name,
        String procedure_name,
        String agent_name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "America/Sao_Paulo")
        OffsetDateTime schedule,
        Float value
) {}
