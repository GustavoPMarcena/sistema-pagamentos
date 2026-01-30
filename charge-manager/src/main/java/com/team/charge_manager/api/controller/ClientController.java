package com.team.charge_manager.api.controller;

import com.team.charge_manager.api.dto.ClientResponse;
import com.team.charge_manager.api.dto.CreateClientRequest;
import com.team.charge_manager.service.ClientAppService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientAppService service;

    public ClientController(ClientAppService service) {
        this.service = service;
    }

    @PostMapping
    public ClientResponse create(@RequestBody CreateClientRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<ClientResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ClientResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
