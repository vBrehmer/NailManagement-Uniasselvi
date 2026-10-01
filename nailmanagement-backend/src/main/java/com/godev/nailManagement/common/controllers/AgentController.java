package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.common.dtos.AgentRequest;
import com.godev.nailManagement.common.dtos.AgentResponse;
import com.godev.nailManagement.domain.agent.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public List<AgentResponse> findAll() {
        return agentService.findAll();
    }

    @GetMapping(params = "id")
    public AgentResponse findById(@RequestParam UUID id) {
        return agentService.findById(id);
    }

     
    @PostMapping
    public AgentResponse save(@RequestBody AgentRequest request) {
        return agentService.save(request);
    }

     
    @DeleteMapping
    public void deleteById(@RequestParam UUID id) {
        agentService.deleteById(id);
    }

     
    @PutMapping
    public AgentResponse put(@RequestBody AgentRequest request) {
        return agentService.put(request);
    }

     
    @PatchMapping
    public AgentResponse patch(@RequestParam UUID id, @RequestBody AgentRequest request) {
        return agentService.patch(id, request);
    }

}
