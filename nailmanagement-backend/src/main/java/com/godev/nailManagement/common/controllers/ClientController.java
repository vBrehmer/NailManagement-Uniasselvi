package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.common.dtos.ClientRequest;
import com.godev.nailManagement.common.dtos.ClientResponse;
import com.godev.nailManagement.common.dtos.PasswordRequest;
import com.godev.nailManagement.domain.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


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
