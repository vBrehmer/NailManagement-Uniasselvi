package com.godev.nailManagement.domain.authapiintegration;

import com.godev.nailManagement.common.AuthApiProperties;
import com.godev.nailManagement.common.dtos.ClientRequest;
import com.godev.nailManagement.common.dtos.ClientResponse;
import com.godev.nailManagement.common.dtos.EmailIntegrationRequest;
import com.godev.nailManagement.common.exceptions.ClientNotFoundException;
import com.godev.nailManagement.domain.client.Client;
import com.godev.nailManagement.domain.client.ClientRepository;
import com.godev.nailManagement.infra.common.dto.AuthRequest;
import com.godev.nailManagement.infra.common.dto.AuthResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class AuthApiIntegrationService {

    private final ClientRepository  clientRepository;
    private final AuthApiProperties authApiProperties;
    private final RestTemplate restTemplate;

    public AuthApiIntegrationService(ClientRepository clientRepository, AuthApiProperties authApiProperties, RestTemplate restTemplate) {
        this.clientRepository = clientRepository;
        this.authApiProperties = authApiProperties;
        this.restTemplate = restTemplate;
    }

    // AuthApi has Bearer Token Authentication. This method will take info from Application.properties to login and recieve the Bearer token;
    public String recieveAuthApiToken() {
        String url = authApiProperties.getUrl() + "/auth/login";
        String UserName = authApiProperties.getUsername();
        String password = authApiProperties.getPassword();

        AuthRequest request = new AuthRequest(UserName, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponse> token = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                AuthResponse.class
        );

        if (!token.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Invalid credentials!");
        }

        return token.getBody().token();
    }

    /*
        AuthApi CRUD integration under;
     */

    public ClientResponse createUserInAuthApi(ClientRequest request) {
        String url = authApiProperties.getUrl() + "/Client?";

        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClientRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                ClientResponse.class
        ).getBody();
    }

    public ResponseEntity<Boolean> deleteUserInAuthApi(UUID id) {
        String url = authApiProperties.getUrl() +"/%s".formatted(id.toString());
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return  restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class);
    }

    public ClientResponse putUserInAuthApi(ClientRequest request) {
        String url = authApiProperties.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());

        HttpEntity<ClientRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(url, HttpMethod.PUT, entity, ClientResponse.class).getBody();
    }

    public ClientResponse patchUserInAuthApi(ClientRequest request) {
        String url = authApiProperties.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());

        HttpEntity<ClientRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(url, HttpMethod.PATCH, entity, ClientResponse.class).getBody();
    }

     /*
        AuthApi CRUD integration above;
     */


    // Send the code;
    public void sendEmailConfirmation(EmailIntegrationRequest email) {
        String url = authApiProperties.getUrl() + "/email/code/send?";
        url += String.format("UserName=%s&email=%s", email.userName(), email.userEmail());

        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Void.class);
    }

    // Used with the sent code, to validate the email address;
    public boolean recieveEmailConfirmation(EmailIntegrationRequest email) {
        if (email.givenCode() == null || email.givenCode().isBlank()) {
            throw new IllegalArgumentException("'givenCode' not informed or blank!");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(recieveAuthApiToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmailIntegrationRequest> entity = new HttpEntity<>(email, headers);

        String url = authApiProperties.getUrl() + "email/code/validate?";
        url += String.format("UserName=%s&email=%s&givenCode=%s", email.userName(), email.userEmail(), email.givenCode());

        ResponseEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        if (Boolean.TRUE.equals(result.getBody())) {
            Client client = clientRepository.findByUsername(email.userName()).orElseThrow(() -> new ClientNotFoundException("Client not found!"));
            client.setEmail_validated(true);
        }

        return Boolean.TRUE.equals(result.getBody());
    }
}
