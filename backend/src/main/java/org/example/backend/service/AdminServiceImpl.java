package org.example.backend.service;


import lombok.RequiredArgsConstructor;
import org.example.backend.config.JwtUtil;
import org.example.backend.dto.AdminRegistrationRequest;
import org.example.backend.dto.AuthenticationRequest;
import org.example.backend.dto.AuthenticationResponse;
import org.example.backend.entity.Admin;
import org.example.backend.exception.ConflictException;
import org.example.backend.repository.AdminRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public void register(AdminRegistrationRequest request) {
        if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Admin with username " + request.getUsername() + " already exists");
        }
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        adminRepository.save(admin);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
}
