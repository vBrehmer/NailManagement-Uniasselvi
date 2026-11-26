package com.godev.nailManagement.domain.client.queryDtos;

import java.util.UUID;

public record MostDoneProcedures(
        Integer p_count,
        String p_name,
        UUID p_id,
        String c_name,
        UUID c_id
) {}
