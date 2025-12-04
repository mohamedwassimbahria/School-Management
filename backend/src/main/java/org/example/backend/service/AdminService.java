package org.example.backend.service;


import org.example.backend.dto.AdminRegistrationRequest;
import org.example.backend.dto.AuthenticationRequest;
import org.example.backend.dto.AuthenticationResponse;

public interface AdminService {
    void register(AdminRegistrationRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
