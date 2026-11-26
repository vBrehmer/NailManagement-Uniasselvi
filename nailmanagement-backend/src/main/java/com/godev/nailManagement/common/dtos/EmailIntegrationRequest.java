package com.godev.nailManagement.common.dtos;

public record EmailIntegrationRequest(
        String userName,
        String userEmail,
        String givenCode
) {
}
