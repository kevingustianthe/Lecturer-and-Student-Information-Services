package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.LoginRequest;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.TokenResponse;
import com.fikupnvj.restfulapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Account> register(@RequestBody Account request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verify")
    public ApiResponse<Account> verify(@RequestParam String email, @RequestParam String code) {
        return authService.verify(email, code);
    }
}
