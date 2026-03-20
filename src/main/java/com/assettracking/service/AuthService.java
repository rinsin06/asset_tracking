package com.assettracking.service;

import com.assettracking.dto.LoginRequest;
import com.assettracking.dto.LoginResponse;
import com.assettracking.model.User;
import com.assettracking.repository.UserRepository;
import com.assettracking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RedisTokenBlacklistService blacklistService;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (!jwtUtil.isTokenExpired(jwt)) {
                Date expiry = jwtUtil.extractExpiration(jwt);
                long ttl = expiry.getTime() - System.currentTimeMillis();
                if (ttl > 0) {
                    blacklistService.blacklist(jwt, ttl);
                }
            }
        }
    }
}