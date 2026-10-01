package com.godev.nailManagement.common.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AgendaRequest(
        UUID client_id,
        UUID procedure_id,
        UUID agent_id,
        OffsetDateTime schedule,
        Float value
) {}
