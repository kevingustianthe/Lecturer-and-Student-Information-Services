package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.AccountResponse;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.UpdateAccountRequest;
import com.fikupnvj.restfulapi.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ApiResponse<List<AccountResponse>> getAll(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token) {
        try {
            return accountService.getAll();
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

//    @GetMapping("/me")
//    public ApiResponse<AccountResponse> getMe(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
//        return accountService.getMe(account);
//    }

    @GetMapping("/{id}")
    public ApiResponse<AccountResponse> getById(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return accountService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<AccountResponse>> search(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "role", required = false, defaultValue = "") String role,
            @RequestParam(name = "status", required = false, defaultValue = "") Boolean status,
            @RequestParam(name = "sortBy", required = false, defaultValue = "email") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        return accountService.search(email, role, status, sortBy, order);
    }

//    @PostMapping("/update/me")
//    public ApiResponse<AccountResponse> updateMe(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @RequestBody UpdateAccountRequest request) {
//        return accountService.updateMe(account, request);
//    }

    @PutMapping("/update/{id}")
    public ApiResponse<AccountResponse> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody UpdateAccountRequest request) {
        return accountService.update(id, request);
    }

    @DeleteMapping("/delete")
    public ApiResponse<AccountResponse> deleteAccount(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody UpdateAccountRequest request) {
        return accountService.deleteAccount(request);
    }
}
