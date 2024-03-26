package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.model.LoginRequest;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.TokenResponse;
import com.fikupnvj.restfulapi.repository.AccountRepository;
import com.fikupnvj.restfulapi.repository.LecturerRepository;
import com.fikupnvj.restfulapi.tool.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@EnableAsync
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LecturerRepository lecturerRepository;

    public ApiResponse<Account> register(Account request) {
        try {
            if (accountRepository.existsById(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
            }

            if (!emailService.checkValidEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
            }

            if (lecturerRepository.findByEmail(request.getEmail()).isPresent()) {
                request.setRole("LECTURER");
            } else {
                request.setRole("STUDENT");
            }

            request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            request.setStatus(false);
            request.setVerificationCode(UUID.randomUUID().toString());

            emailService.sendVerificationEmail(request);
            accountRepository.save(request);
            return new ApiResponse<>(true, "Account created successfully. Check your email to verify your account", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    public ApiResponse<TokenResponse> login (LoginRequest request) {
        try {
            Account account = accountRepository.findById(request.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password invalid"));

            if (!account.getStatus()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not verified");
            }

            if (BCrypt.checkpw(request.getPassword(), account.getPassword())) {
                account.setToken(UUID.randomUUID().toString());
                account.setTokenExpiredAt(System.currentTimeMillis() + (60 * 60 * 1000));
                accountRepository.save(account);

                return new ApiResponse<>(true, "Login Success", new TokenResponse(account.getToken(), account.getTokenExpiredAt()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password invalid");
            }
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }

    }

    public ApiResponse<Account> verify(String email, String code) {
        try {
            Account account = accountRepository.findById(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            if (!Objects.equals(account.getVerificationCode(), code)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code");
            }

            account.setStatus(true);
            account.setVerificationCode(null);
            accountRepository.save(account);

            return new ApiResponse<>(true, "Account successfully verified", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }

    }
}
