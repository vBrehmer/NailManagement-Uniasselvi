package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.domain.followup.FollowUpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/followup")
public class FollowUpController {

    private final FollowUpService followUpService;

    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    @GetMapping
    public void sendFollowUps() {
        followUpService.sendFollowUps();
    }

}
