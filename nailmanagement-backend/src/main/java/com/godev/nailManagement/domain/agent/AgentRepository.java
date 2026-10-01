package com.godev.nailManagement.domain.agent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findByNameIgnoreCase(String name);

}
