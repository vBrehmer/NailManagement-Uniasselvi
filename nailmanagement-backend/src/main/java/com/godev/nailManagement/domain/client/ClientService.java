package com.godev.nailManagement.domain.client;

import com.godev.nailManagement.common.dtos.ClientRequest;
import com.godev.nailManagement.common.dtos.ClientResponse;
import com.godev.nailManagement.common.dtos.PasswordRequest;
import com.godev.nailManagement.common.exceptions.ClientNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ClientService(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /*
        NailManagement CRUD under;
     */

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ClientResponse findById(UUID id) {
        return toResponse(clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found!")));
    }

    public ClientResponse save(ClientRequest request) {

        Client client = new Client();
        client.setUsername(request.username());
        client.setEmail(request.email());
        client.setEmail_validated(false);
        if (request.password() != null) {
            client.setPassword(passwordEncoder.encode(request.password()));
        } else {
            client.setPassword(passwordEncoder.encode("123456"));
        }
        client.setRole(request.role());
        client.setLast_email_sent(OffsetDateTime.now());

        Client response = clientRepository.save(client);

        return toResponse(response);
    }

    public ClientResponse put(UUID id, ClientRequest request) {
        Client client =  clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found!"));

        if (passwordEncoder.matches(request.password(), client.getPassword())) {
            client.setUsername(request.username());
            client.setEmail(request.email());
            client.setRole(request.role());

            return toResponse(clientRepository.save(client));
        } else {
            throw new IllegalArgumentException("Wrong password!");
        }
    }

    public ClientResponse patch(UUID id, ClientRequest request) {
        validatePatch(request);
        Client client = clientRepository.findById(id).orElseThrow(() ->  new ClientNotFoundException("Client not found!"));

        if (request.password() != null) {
            client.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.username() != null) {
            client.setUsername(request.username());
        }
        if (request.email() != null && !request.email().equals(client.getEmail())) {
            client.setEmail(request.email());
        }
        if (request.role() != null) {
            client.setRole(request.role());
        }
        return toResponse(clientRepository.save(client));
    }

    public ClientResponse changePassword(UUID client_id, PasswordRequest request) {
        Client client =  clientRepository.findById(client_id).orElseThrow(() -> new ClientNotFoundException("Client not found!"));
        if (passwordEncoder.matches(request.old_password(), client.getPassword())) {
            client.setPassword(passwordEncoder.encode(request.new_password()));
            return toResponse(client);
        } else {
            throw new IllegalArgumentException("Wrong password!");
        }
    }

    public void deleteById(UUID id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new ClientNotFoundException("Client not found!");
        }
    }

    /*
        NailManagement CRUD above;
     */

    // Internal method for converting into response record;
    private ClientResponse toResponse (Client client) {
        return new ClientResponse(
                client.getId(),
                client.getUsername(),
                client.getRole().toString(),
                client.getEmail(),
                client.getLast_email_sent()
        );
    }

    private void validatePatch(ClientRequest request) {
        if (request.username() != null && request.username().isBlank()) {
            throw new IllegalArgumentException("'UserName' not informed or blank!");
        }
        if (request.email() != null && request.email().isBlank()) {
            throw new IllegalArgumentException("'email' not informed or blank!");
        }
        if (request.password() != null && request.password().isBlank()) {
            throw new IllegalArgumentException("'password' not informed or blank!");
        }
        if (request.role() != null && request.role().toString().isBlank()) {
            throw new IllegalArgumentException("'role' needs to be 'Client' or 'ADMIN'");
        }
    }
}
