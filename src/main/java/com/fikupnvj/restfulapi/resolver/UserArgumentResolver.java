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

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Account.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader("X-API-TOKEN");

        Account account = accountRepository.findFirstByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        if (account.getTokenExpiredAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return account;
    }
}
