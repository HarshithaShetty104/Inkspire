package com.example.Authentication.Controller;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import com.example.Authentication.Security.SpringSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.Authentication.DTO.JwtAuthResponse;
import com.example.Authentication.DTO.LoginDto;
import com.example.Authentication.DTO.MessageResponse;
import com.example.Authentication.DTO.SignUpRequest;

import com.example.Authentication.Entity.Role;
import com.example.Authentication.Entity.User;
import com.example.Authentication.Repository.RoleRepository;
import com.example.Authentication.Repository.UserRepository;
import com.example.Authentication.Service.AuthService;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                             signUpRequest.getEmail(),
                             encoder.encode(signUpRequest.getPassword()));

        Set<String> roleNames = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();


        if (roleNames == null || roleNames.isEmpty()) {
            Role defaultRole = roleRepository.findByName("READER")
                                              .orElseThrow(() -> new RuntimeException("Error: Default role not found."));
            roles.add(defaultRole);
        } else {
            roleNames.forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                                          .orElseThrow(() -> new RuntimeException("Error: Role not found: " + roleName));
                roles.add(role);
            });
        }

        user.setRoles(roles);
         userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));



    //     if (strRoles == null) {
    //         Role userRole = roleRepository.findByName(roles)
    //                 .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //         roles.add(userRole);
    //     } else {
    //         strRoles.forEach(role -> {
    //             switch (role) {
    //             case "admin":
    //                 Role adminRole = roleRepository.findByName(roles)
    //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //                 roles.add(adminRole);

    //                 break;
    //             case "mod":
    //                 Role modRole = roleRepository.findByName(roles)
    //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //                 roles.add(modRole);

    //                 break;
    //             default:
    //                 Role userRole = roleRepository.findByName(roles)
    //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //                 roles.add(userRole);
    //             }
    //         });
    //     }

    //     user.setRoles(roles);
    //     userRepository.save(user);

    //     return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    // }

}}