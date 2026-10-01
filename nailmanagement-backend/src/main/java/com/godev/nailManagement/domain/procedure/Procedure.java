package com.godev.nailManagement.domain.procedure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float value;

}
