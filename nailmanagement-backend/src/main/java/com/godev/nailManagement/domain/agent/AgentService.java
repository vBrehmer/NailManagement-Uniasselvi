package com.godev.nailManagement.domain.agent;

import com.godev.nailManagement.common.dtos.AgentRequest;
import com.godev.nailManagement.common.dtos.AgentResponse;
import com.godev.nailManagement.common.exceptions.AgentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<AgentResponse> findAll() {
        return agentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AgentResponse findById(UUID id) {
        return toResponse(agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("Agent not found!")));
    }

    public AgentResponse save(AgentRequest request) {
        Agent agent =  new Agent();
        agent.setName(request.name());
        agent.setArea(request.area());

        return toResponse(agentRepository.save(agent));
    }

    public AgentResponse put(AgentRequest request) {
        Agent a = agentRepository.findByNameIgnoreCase(request.name()).orElseThrow(() -> new AgentNotFoundException("Agent not found!"));
        a.setName(request.name());
        a.setArea(request.area());
        return toResponse(agentRepository.save(a));
    }

    public AgentResponse patch(UUID id, AgentRequest request) {
        validatePatch(request);
        Agent a = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("Agent not found!"));
        if (request.name() != null && !request.name().isBlank()) {
            a.setName(request.name());
        }
        if (request.area() != null && !request.area().isBlank()) {
            a.setArea(request.area());
        }
        return toResponse(agentRepository.save(a));
    }

    public void deleteById(UUID id) {
        if (agentRepository.existsById(id)) {
            agentRepository.deleteById(id);
        } else {
            throw new AgentNotFoundException("Agent not found!");
        }
    }

    private void validatePatch(AgentRequest request) {
        if (request.name() !=  null && request.name().isBlank()) {
            throw new IllegalArgumentException("'username' must not be blank!");
        }
        if (request.area() !=  null && request.area().isBlank()) {
            throw new IllegalArgumentException("'area' must not be blank!");
        }
    }

    private AgentResponse toResponse (Agent agent) {
        return new AgentResponse(
                agent.getId(),
                agent.getName(),
                agent.getArea()
        );
    }

}
