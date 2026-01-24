package com.team.charge_manager.api.controller;

import com.team.charge_manager.api.dto.ChargeResponse;
import com.team.charge_manager.api.dto.CreateChargeRequest;
import com.team.charge_manager.service.ChargeAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charges")
public class ChargeController {

    private final ChargeAppService chargeService;

    public ChargeController(ChargeAppService chargeService) {
        this.chargeService = chargeService;
    }

    @PostMapping
    public ResponseEntity<ChargeResponse> create(@Valid @RequestBody CreateChargeRequest request) {
        return ResponseEntity.ok(chargeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chargeService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChargeResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(chargeService.cancel(id));
    }
}
