package org.example.backend;


import org.example.backend.config.JwtUtil;
import org.example.backend.dto.AdminRegistrationRequest;
import org.example.backend.dto.AuthenticationRequest;
import org.example.backend.entity.Admin;
import org.example.backend.exception.ConflictException;
import org.example.backend.repository.AdminRepository;
import org.example.backend.service.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void register_shouldRegisterNewAdmin() {
        AdminRegistrationRequest request = new AdminRegistrationRequest();
        request.setUsername("admin");
        request.setPassword("password");
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        adminService.register(request);

        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void register_shouldThrowConflictException_whenAdminAlreadyExists() {
        AdminRegistrationRequest request = new AdminRegistrationRequest();
        request.setUsername("admin");
        request.setPassword("password");
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(new Admin()));

        assertThrows(ConflictException.class, () -> adminService.register(request));
    }

    @Test
    void login_shouldReturnJwtToken() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("admin");
        request.setPassword("password");
        UserDetails userDetails = new User("admin", "password", java.util.Collections.emptyList());
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);

        adminService.login(request);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("admin", "password"));
        verify(jwtUtil).generateToken(userDetails);
    }
}
