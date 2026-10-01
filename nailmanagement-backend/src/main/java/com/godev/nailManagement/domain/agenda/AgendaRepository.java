package com.godev.nailManagement.domain.agenda;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

    List<Agenda> findBySchedule(OffsetDateTime offsetDateTime);
    List<Agenda> findByScheduleBetweenOrderByScheduleAsc(OffsetDateTime start, OffsetDateTime end);

}
