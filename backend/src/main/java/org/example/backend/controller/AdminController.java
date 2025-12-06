package org.example.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AdminRegistrationRequest;
import org.example.backend.dto.AuthenticationRequest;
import org.example.backend.dto.AuthenticationResponse;
import org.example.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Register a new admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin registered successfully"),
            @ApiResponse(responseCode = "409", description = "Admin with the same username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AdminRegistrationRequest request) {
        adminService.register(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Login as an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }
}
