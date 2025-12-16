package eco.kosova.presentation.api.controllers;

import eco.kosova.infrastructure.persistence.jpa.UserEntity;
import eco.kosova.infrastructure.persistence.jpa.UserEntityRepository;
import eco.kosova.infrastructure.security.JwtTokenProvider;
import eco.kosova.presentation.dtos.AuthResponse;
import eco.kosova.presentation.dtos.LoginRequest;
import eco.kosova.presentation.dtos.RegisterRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserEntityRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        logger.info("POST /api/auth/login - username: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
            
            AuthResponse response = new AuthResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                tokenProvider.getExpirationTime() / 1000 // Convert to seconds
            );
            
            logger.info("User {} logged in successfully", request.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Invalid username or password", e);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        logger.info("POST /api/auth/register - username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed: username already exists - {}", request.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed: email already exists - {}", request.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setEnabled(true);
        user.setCreatedAt(Instant.now());
        user.setLastUpdated(Instant.now());
        
        user = userRepository.save(user);
        
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        
        AuthResponse response = new AuthResponse(
            token,
            "Bearer",
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            tokenProvider.getExpirationTime() / 1000
        );
        
        logger.info("User {} registered successfully", request.getUsername());
        return ResponseEntity.ok(response);
    }
}

