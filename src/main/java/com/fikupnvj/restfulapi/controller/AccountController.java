package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.AccountResponse;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.UpdateAccountRequest;
import com.fikupnvj.restfulapi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ApiResponse<List<AccountResponse>> getAll(Account admin) {
        return accountService.getAll(admin);
    }

    @GetMapping("/me")
    public ApiResponse<AccountResponse> getMe(Account account) {
        return accountService.getMe(account);
    }

    @PostMapping("/find")
    public ApiResponse<AccountResponse> getById(Account admin, @RequestBody AccountResponse accountResponse) {
        return accountService.getById(admin, accountResponse.getEmail());
    }

    @GetMapping("/search")
    public ApiResponse<List<AccountResponse>> getByParam(Account account,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "role", required = false, defaultValue = "") String role,
            @RequestParam(name = "status", required = false, defaultValue = "") Boolean status) {
        return accountService.getByParam(email, role, status);
    }

    @PostMapping("/update/me")
    public ApiResponse<AccountResponse> updateMe(Account account, @RequestBody UpdateAccountRequest request) {
        return accountService.updateMe(account, request);
    }

    @PostMapping("/update")
    public ApiResponse<AccountResponse> updateById(Account admin, @RequestBody UpdateAccountRequest request) {
        return accountService.updateById(admin, request);
    }

    @DeleteMapping("/delete")
    public ApiResponse<AccountResponse> deleteAccount(Account admin, @RequestBody UpdateAccountRequest request) {
        return accountService.deleteAccount(admin, request);
    }
}
