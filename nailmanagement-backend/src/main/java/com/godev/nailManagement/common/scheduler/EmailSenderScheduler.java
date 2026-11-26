package com.godev.nailManagement.common.scheduler;

import com.godev.nailManagement.domain.client.ClientService;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderScheduler {

    private final ClientService clientService;

    public EmailSenderScheduler(ClientService clientService) {
        this.clientService = clientService;
    }




}
