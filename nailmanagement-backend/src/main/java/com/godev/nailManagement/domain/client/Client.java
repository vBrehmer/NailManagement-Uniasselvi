package com.godev.nailManagement.domain.client;

import com.godev.nailManagement.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid set default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "boolean set default false")
    private boolean email_validated;

    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime last_email_sent;

    @Enumerated(EnumType.STRING)
    private Role role;

}
