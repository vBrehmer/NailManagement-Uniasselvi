package com.godev.nailManagement.common.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse(
   UUID id,
   String username,
   String role,
   String email,
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "America/Sao_Paulo")
   OffsetDateTime last_email_sent
) {}
