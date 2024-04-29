package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.UpdateAccountRequest;
import com.fikupnvj.restfulapi.model.AccountResponse;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Object> getAll() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountResponse> accountResponses = toListAccountResponse(accountList);

        ApiResponse<List<AccountResponse>> response = new ApiResponse<>(true, "Account successfully retrieved", accountResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getById(String email) {
        Account acc = accountRepository.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        AccountResponse accountResponse = toResponseAccount(acc);

        ApiResponse<AccountResponse> response = new ApiResponse<>(true, "Account successfully found", accountResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> search(String email, String role, Boolean status, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(order)));
        List<Account> accounts = accountRepository.findAll(sort);

        if (!Objects.equals(email, "")) {
            accounts = accounts.stream().filter(
                    account -> account.getEmail().toLowerCase().contains(email.toLowerCase())
            ).toList();
        }

        if (!Objects.equals(role, "")) {
            accounts = accounts.stream().filter(
                    account -> account.getRole().equalsIgnoreCase(role)
            ).toList();
        }

        if (status != null) {
            accounts = accounts.stream().filter(
                    account -> account.getStatus().equals(status)
            ).toList();
        }

        ApiResponse<List<AccountResponse>> response = new ApiResponse<>(true, "Data successfully retrieved", toListAccountResponse(accounts));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> update(String id, UpdateAccountRequest request) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (Objects.nonNull(request.getRole())) {
            acc.setRole(request.getRole());
        }

        if (Objects.nonNull(request.getStatus())) {
            acc.setStatus(request.getStatus());
            if (request.getStatus()) {
                acc.setVerificationCode(null);
            } else {
                acc.setVerificationCode(UUID.randomUUID().toString());
            }

        }

        accountRepository.save(acc);

        ApiResponse<AccountResponse> response = new ApiResponse<>(true, "Update successfully", toResponseAccount(acc));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> delete(String id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        accountRepository.delete(acc);

        ApiResponse<AccountResponse> response = new ApiResponse<>(true, "Account successfully deleted", toResponseAccount(acc));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AccountResponse toResponseAccount(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail(account.getEmail());
        accountResponse.setRole(account.getRole());
        accountResponse.setStatus(account.getStatus());

        return accountResponse;
    }

    private List<AccountResponse> toListAccountResponse(List<Account> accounts) {
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            AccountResponse accountResponse = toResponseAccount(account);
            accountResponses.add(accountResponse);
        }

        return accountResponses;
    }
}
