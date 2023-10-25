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
    public ApiResponse<AccountResponse> getCurrent(Account account) {
        return accountService.getCurrent(account);
    }

    @PostMapping("/find")
    public ApiResponse<AccountResponse> getById(Account admin, @RequestBody AccountResponse accountResponse) {
        return accountService.getById(admin, accountResponse.getEmail());
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
