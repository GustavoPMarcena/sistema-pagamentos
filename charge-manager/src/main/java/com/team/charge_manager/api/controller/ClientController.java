package com.team.charge_manager.api.controller;

import com.team.charge_manager.api.dto.ClientResponse;
import com.team.charge_manager.api.dto.CreateClientRequest;
import com.team.charge_manager.service.ClientAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientAppService clientService;

    public ClientController(ClientAppService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.ok(clientService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }
}
