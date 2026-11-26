package com.godev.nailManagement.domain.client.queryDtos;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MaxScheduleDateTimeQuery(
        Integer inactive_days,
        UUID client_id,
        String username,
        LocalDateTime last_schedule
) {}
