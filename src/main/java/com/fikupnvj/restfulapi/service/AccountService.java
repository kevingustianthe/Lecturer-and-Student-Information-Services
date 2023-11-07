package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.UpdateAccountRequest;
import com.fikupnvj.restfulapi.model.AccountResponse;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.AccountRepository;
import com.fikupnvj.restfulapi.tool.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    public ApiResponse<List<AccountResponse>> getAll(Account account) {
        List<Account> accountList = accountRepository.findAll();
        List<AccountResponse> accountResponses = toListAccountResponse(accountList);

        return new ApiResponse<>(true, "Account successfully retrieved", accountResponses);
    }

    public ApiResponse<AccountResponse> getMe(Account account) {
        AccountResponse accountResponse = toResponseAccount(account);

        return new ApiResponse<>(true, "Account successfully retrieved", accountResponse);
    }

    public ApiResponse<AccountResponse> getById(Account account, String email) {
        Account acc = accountRepository.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        AccountResponse accountResponse = toResponseAccount(acc);

        return new ApiResponse<>(true, "Account successfully found", accountResponse);
    }

    public ApiResponse<List<AccountResponse>> getByParam(String email, String role, Boolean status, String sortBy, String order) {
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

        return new ApiResponse<>(true, "Data successfully retrieved", toListAccountResponse(accounts));
    }

    public ApiResponse<AccountResponse> updateMe(Account account, UpdateAccountRequest request) {
        if (Objects.nonNull(request.getPassword())) {
            account.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        accountRepository.save(account);

        return new ApiResponse<>(true, "Update successfully", toResponseAccount(account));
    }

    public ApiResponse<AccountResponse> updateById(Account account, UpdateAccountRequest request) {
        Account acc = accountRepository.findById(request.getEmail())
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

        return new ApiResponse<>(true, "Update successfully", toResponseAccount(acc));
    }

    public ApiResponse<AccountResponse> deleteAccount(Account account, UpdateAccountRequest request) {
        Account acc = accountRepository.findById(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        accountRepository.delete(acc);

        return new ApiResponse<>(true, "Account successfully deleted", toResponseAccount(acc));
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
