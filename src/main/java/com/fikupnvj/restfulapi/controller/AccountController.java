package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.UpdateAccountRequest;
import com.fikupnvj.restfulapi.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<Object> getAll(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token) {
        return accountService.getAll();
    }

//    @GetMapping("/me")
//    public ApiResponse<AccountResponse> getMe(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
//        return accountService.getMe(account);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return accountService.getById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token,
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
    public ResponseEntity<Object> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody UpdateAccountRequest request) {
        return accountService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return accountService.delete(id);
    }
}
