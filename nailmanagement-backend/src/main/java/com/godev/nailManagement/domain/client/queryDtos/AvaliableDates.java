package com.godev.nailManagement.domain.client.queryDtos;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AvaliableDates(
        LocalDateTime avaliableDateTime
) {}
