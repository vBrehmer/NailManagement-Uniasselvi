package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.common.dtos.EmailIntegrationRequest;
import com.godev.nailManagement.common.dtos.ClientRequest;
import com.godev.nailManagement.common.dtos.ClientResponse;
import com.godev.nailManagement.common.dtos.PasswordRequest;
import com.godev.nailManagement.domain.authapiintegration.AuthApiIntegrationService;
import com.godev.nailManagement.domain.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthApiIntegrationService authApiIntegrationService;

    @GetMapping(value = "/email/confirmation/send")
    public void sendEmailConfirmation(@RequestBody EmailIntegrationRequest email) {
        authApiIntegrationService.sendEmailConfirmation(email);
    }

    @PostMapping(value = "/email/confirmation/validate")
    public boolean validateEmailConfirmation(@RequestBody EmailIntegrationRequest email) {
        return authApiIntegrationService.recieveEmailConfirmation(email);
    }

    @PostMapping("/authapi")
    public ClientResponse registerUserInApi(@RequestBody ClientRequest user) {
        return authApiIntegrationService.createUserInAuthApi(user);
    }

    @PutMapping("/authapi")
    public ClientResponse putUserInApi(@RequestBody ClientRequest user) {
        return authApiIntegrationService.putUserInAuthApi(user);
    }

    @PatchMapping("/authapi")
    public ClientResponse patchUserInAuthApi(@RequestBody ClientRequest user) {
        return authApiIntegrationService.patchUserInAuthApi(user);
    }

    @DeleteMapping("/authapi/{id}")
    public ResponseEntity<Boolean> deleteUserInAuthApi(@PathVariable UUID id) {
        return authApiIntegrationService.deleteUserInAuthApi(id);
    }

    /*
        \start
        Auxiliar endpoints to help front-end;
     */

    @GetMapping("/email/validated")
    public List<ClientResponse> allEmailsValidated() {
        return clientService.findAllValidatedEmails();
    }

    @GetMapping(value = "/email/validated", params = "id")
    public boolean isEmailValidated(@RequestParam UUID id) {
        return clientService.isEmailValidated(id);
    }

    @GetMapping("/email/validated/false")
    public List<ClientResponse> allEmailsNotValidated() {
        return clientService.findAllNotValidatedEmails();
    }

    /*
        \end
        Auxiliar endpoints to help front-end;
     */

    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.findAll();
    }

    @GetMapping(params = "id")
    public ClientResponse findById(@RequestParam UUID id) {
        return clientService.findById(id);
    }

     
    @PostMapping()
    public ClientResponse registerUser(@RequestBody ClientRequest user) {
        return clientService.save(user);
    }

     
    @DeleteMapping
    public void deleteUser(@RequestParam UUID id) {
        clientService.deleteById(id);
    }

     
    @PutMapping()
    public ClientResponse putUser(@RequestParam UUID id, @RequestBody ClientRequest user) {
        return clientService.put(id, user);
    }

    @PutMapping("/password")
    public ClientResponse changePassword(@RequestParam UUID id, @RequestBody PasswordRequest request) {
        return clientService.changePassword(id, request);
    }

     
    @PatchMapping
    public ClientResponse patchUser(@RequestParam UUID id, @RequestBody ClientRequest user) {
        return clientService.patch(id, user);
    }

}
