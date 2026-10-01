package com.godev.nailManagement.domain.procedure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcedureRepository extends JpaRepository<Procedure, UUID> {

    Optional<Procedure> findByNameIgnoreCase(String name);

}
