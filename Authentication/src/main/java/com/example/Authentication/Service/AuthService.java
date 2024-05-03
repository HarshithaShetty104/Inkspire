package com.example.Authentication.Service;

import com.example.Authentication.DTO.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
