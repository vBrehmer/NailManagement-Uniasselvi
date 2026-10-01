package com.godev.nailManagement.domain.agent;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid set default gen_random_uuid()")
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column
    private String area;

}
