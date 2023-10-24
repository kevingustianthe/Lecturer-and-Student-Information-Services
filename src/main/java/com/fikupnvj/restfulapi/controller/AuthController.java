package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.LoginRequest;
import com.fikupnvj.restfulapi.model.ResponseData;
import com.fikupnvj.restfulapi.model.TokenData;
import com.fikupnvj.restfulapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseData<Account> register(@RequestBody Account request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseData<TokenData> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verify")
    public ResponseData<Account> verify(@RequestParam String email, @RequestParam String code) {
        return authService.verify(email, code);
    }
}
