package com.godev.nailManagement.domain.client.listener;

import com.godev.nailManagement.common.dtos.EmailIntegrationRequest;
import com.godev.nailManagement.domain.authapiintegration.AuthApiIntegrationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClientRegisteredListener {

    AuthApiIntegrationService authApiIntegrationService;
    TaskScheduler taskScheduler;

    public ClientRegisteredListener(AuthApiIntegrationService authApiIntegrationService, TaskScheduler taskScheduler) {
        this.authApiIntegrationService = authApiIntegrationService;
        this.taskScheduler = taskScheduler;
    }

    @Async("taskExecutor")
    @EventListener
    public void onUserRegistered(ClientRegisteredEvent event) {
        taskScheduler.schedule(
                () -> authApiIntegrationService.sendEmailConfirmation(new EmailIntegrationRequest(event.username(), event.email(), null)),
                Instant.now().plusSeconds(5)
        );
    }

}
