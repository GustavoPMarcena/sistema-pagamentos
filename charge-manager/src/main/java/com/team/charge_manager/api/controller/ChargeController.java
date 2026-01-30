package com.team.charge_manager.api.controller;

import com.team.charge_manager.api.dto.ChargeResponse;
import com.team.charge_manager.api.dto.CreateChargeRequest;
import com.team.charge_manager.service.ChargeAppService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges")
public class ChargeController {

    private final ChargeAppService service;

    public ChargeController(ChargeAppService service) {
        this.service = service;
    }

    @PostMapping
    public ChargeResponse create(@RequestBody CreateChargeRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<ChargeResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ChargeResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/{id}/cancel")
    public ChargeResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}
