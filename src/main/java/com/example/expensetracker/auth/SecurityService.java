package com.example.expensetracker.auth;

import com.example.expensetracker.auth.models.Credentials;
import com.example.expensetracker.auth.models.SecurityProperties;
import com.example.expensetracker.auth.models.UserPrincipal;
import com.example.expensetracker.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class SecurityService {
    HttpServletRequest httpServletRequest;

    CookieUtils cookieUtils;

    SecurityProperties securityProperties;

    @Autowired
    public SecurityService(HttpServletRequest httpServletRequest, CookieUtils cookieUtils, SecurityProperties securityProperties) {
        this.httpServletRequest = httpServletRequest;
        this.cookieUtils = cookieUtils;
        this.securityProperties = securityProperties;
    }

    public UserPrincipal getUserPrincipal() {
        UserPrincipal userPrincipal = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            userPrincipal = (UserPrincipal) principal;
        }
        return userPrincipal;
    }

    public Credentials getCredentials() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object credentials = securityContext.getAuthentication().getCredentials();
        return (Credentials) credentials;
    }

    public boolean isPublic() {
        return securityProperties.getAllowedPublicApis().contains(httpServletRequest.getRequestURI());
    }

    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }

        return bearerToken;
    }
}
