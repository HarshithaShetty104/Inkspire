package com.example.Authentication.DTO;

import java.util.Set;

import com.example.Authentication.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    
   
    private String username;

   
    private String password;


   
    private String email;

    private Set<String> roles;

    // Getters and Setters
}
