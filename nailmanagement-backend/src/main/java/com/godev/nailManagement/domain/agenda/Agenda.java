package com.godev.nailManagement.domain.agenda;

import com.godev.nailManagement.domain.agent.Agent;
import com.godev.nailManagement.domain.procedure.Procedure;
import com.godev.nailManagement.domain.client.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime schedule;

    @Column(nullable = false)
    private Float value;

}
