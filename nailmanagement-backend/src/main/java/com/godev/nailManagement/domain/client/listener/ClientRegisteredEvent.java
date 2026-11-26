package com.godev.nailManagement.domain.client.listener;

public record ClientRegisteredEvent(
        String username,
        String email)
{}
