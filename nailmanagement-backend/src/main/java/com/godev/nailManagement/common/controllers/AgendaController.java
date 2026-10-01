package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.common.dtos.AgendaRequest;
import com.godev.nailManagement.common.dtos.AgendaResponse;
import com.godev.nailManagement.domain.agenda.Agenda;
import com.godev.nailManagement.domain.agenda.AgendaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public List<AgendaResponse> findAll() {
        return agendaService.findAll();
    }

    @GetMapping(params = "schedule")
    public List<AgendaResponse> findAllBySchedule(@RequestParam OffsetDateTime schedule) {
        return agendaService.findAllBySchedule(schedule);
    }

    @GetMapping(params = "schedule, between")
    public List<AgendaResponse> findAllByScheduleBetween(@RequestParam OffsetDateTime schedule,@RequestParam Boolean between) {
        return between ? agendaService.findAllBySchedule(schedule) : List.of();
    }
     
    @PostMapping
    public AgendaResponse save(@RequestBody AgendaRequest request) {
        return agendaService.save(request);
    }

     
    @DeleteMapping
    public void delete(@RequestParam UUID id) {
        agendaService.deleteById(id);
    }

     
    @PutMapping
    public AgendaResponse put(@RequestParam UUID id, @RequestBody AgendaRequest request) {
        return agendaService.put(id,request);
    }

     
    @PatchMapping
    public AgendaResponse patch(@RequestParam UUID id, @RequestBody AgendaRequest request) {
        return agendaService.patch(id, request);
    }


}
