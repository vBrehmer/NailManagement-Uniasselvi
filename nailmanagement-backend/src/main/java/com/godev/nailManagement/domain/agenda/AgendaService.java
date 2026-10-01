package com.godev.nailManagement.domain.agenda;

import com.godev.nailManagement.common.dtos.AgendaRequest;
import com.godev.nailManagement.common.dtos.AgendaResponse;
import com.godev.nailManagement.common.exceptions.AgendaNotFoundException;
import com.godev.nailManagement.common.exceptions.AgentNotFoundException;
import com.godev.nailManagement.common.exceptions.ClientNotFoundException;
import com.godev.nailManagement.common.exceptions.ProcedureNotFoundException;
import com.godev.nailManagement.domain.agent.Agent;
import com.godev.nailManagement.domain.agent.AgentRepository;
import com.godev.nailManagement.domain.procedure.ProcedureRepository;
import com.godev.nailManagement.domain.client.ClientRepository;
import org.springframework.stereotype.Service;
import com.godev.nailManagement.domain.procedure.Procedure;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final ClientRepository clientRepository;
    private final ProcedureRepository procedureRepository;
    private final AgentRepository agentRepository;

    public AgendaService(
            AgendaRepository agendaRepository,
            ClientRepository clientRepository,
            ProcedureRepository procedureRepository,
            AgentRepository agentRepository
    ) {
        this.agendaRepository = agendaRepository;
        this.clientRepository = clientRepository;
        this.procedureRepository = procedureRepository;
        this.agentRepository = agentRepository;
    }

    public List<AgendaResponse> findAll() {
        return agendaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AgendaResponse findById(UUID id) {
        return toResponse(agendaRepository.findById(id).orElseThrow(() -> new AgendaNotFoundException("Agenda not found!")));
    }

    public List<AgendaResponse> findAllBySchedule(OffsetDateTime schedule) {
        OffsetDateTime start = schedule.with(LocalTime.MIN);
        OffsetDateTime end = schedule.with(LocalTime.MAX);
        return agendaRepository.findByScheduleBetweenOrderByScheduleAsc(start, end).stream().map(this::toResponse).toList();
    }

    public AgendaResponse save(AgendaRequest request) {
        List<Agenda> agendasNoHorario = agendaRepository.findBySchedule(request.schedule());
        for (Agenda a : agendasNoHorario) {
            if (a.getAgent().getId().equals(request.agent_id())) {
                Agent agent = agentRepository.findById(request.agent_id())
                        .orElseThrow(() -> new AgentNotFoundException("Agent not found!"));

                throw new IllegalStateException(
                        agent.getName() + " já possui este horário agendado! "
                );
            }
        }
        Agenda a = new Agenda();

        Procedure p = procedureRepository.findById(request.procedure_id()).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!"));

        a.setValue(
                request.value() != null && request.value() > 0
                ? request.value()
                : p.getValue()
        );
        a.setClient(clientRepository.findById(request.client_id()).orElseThrow(() -> new ClientNotFoundException("Client not found!")));
        a.setAgent(agentRepository.findById(request.agent_id()).orElseThrow(() -> new AgentNotFoundException("Agent not found!")));
        a.setProcedure(procedureRepository.findById(request.procedure_id()).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!")));
        a.setSchedule(request.schedule());

        return toResponse(agendaRepository.save(a));
    }

    public AgendaResponse put(UUID id, AgendaRequest request) {
        Agenda a = agendaRepository.findById(id).orElseThrow(() ->  new AgendaNotFoundException("Agenda not found!"));
        a.setClient(clientRepository.findById(request.client_id()).orElseThrow(() -> new ClientNotFoundException("User not found!")));
        a.setAgent(agentRepository.findById(request.agent_id()).orElseThrow(() -> new AgentNotFoundException("Agent not found!")));
        a.setProcedure(procedureRepository.findById(request.procedure_id()).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!")));
        if (request.schedule().isBefore(OffsetDateTime.now())) {
            a.setSchedule(request.schedule());
        } else {
            throw new IllegalArgumentException("'schedule' must in the future!");
        }
        a.setValue(request.value());

        return toResponse(agendaRepository.save(a));
    }

    public AgendaResponse patch(UUID id, AgendaRequest request) {
        validatePatch(request);

        Agenda a = agendaRepository.findById(id).orElseThrow(() -> new AgendaNotFoundException("Agenda not found!"));

        if (request.value() != null) {
            a.setValue(request.value());
        }
        if (request.schedule() != null) {
            a.setSchedule(request.schedule());
        }
        if (request.client_id() != null) {
            a.setClient(clientRepository.findById(request.client_id()).orElseThrow(() -> new ClientNotFoundException("User not found!")));
        }
        if (request.agent_id() != null) {
            a.setAgent(agentRepository.findById(request.agent_id()).orElseThrow(() -> new AgentNotFoundException("Agent not found!")));
        }
        if (request.procedure_id() != null) {
            a.setProcedure(procedureRepository.findById(request.procedure_id()).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!")));
        }
        return toResponse(agendaRepository.save(a));
    }

    private void validatePatch(AgendaRequest request) {
//        if (request.schedule() != null && request.schedule().isBefore(OffsetDateTime.now())) {
//            throw new IllegalArgumentException("'schedule' only can be updated to future!");
//        }
        if (request.value() != null && request.value() < 0) {
            throw new IllegalArgumentException("'value' cannot be negative!");
        }
    }

    public void deleteById(UUID id) {
        if (agendaRepository.existsById(id)) {
            agendaRepository.deleteById(id);
        } else {
            throw new AgendaNotFoundException("Agenda not found!");
        }
    }


    private AgendaResponse toResponse(Agenda a) {
        return new AgendaResponse(
                a.getId(),
                a.getClient().getUsername(),
                a.getProcedure().getName(),
                a.getAgent().getName(),
                a.getSchedule(),
                a.getValue()
        );
    }
}
