package com.fikupnvj.restfulapi.resolver;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Component
public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().equals(Account.class))
            return Objects.equals(parameter.getParameterName(), "admin");
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader("X-API-TOKEN");

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Account account = accountRepository.findFirstByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        if (!account.getStatus()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not verified");
        }

        if (account.getTokenExpiredAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!Objects.equals(account.getRole(), "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return account;
    }
}
