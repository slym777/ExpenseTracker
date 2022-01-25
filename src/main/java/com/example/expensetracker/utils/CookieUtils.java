package com.example.expensetracker.utils;

import com.example.expensetracker.auth.SecurityService;
import com.example.expensetracker.auth.models.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieUtils {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private SecurityProperties restSecurityProps;

    public Cookie getCookie(String name) {
        return WebUtils.getCookie(httpServletRequest, name);
    }

    public void setCookie(String name, String value, int expiryInMinutes) {
        int expiryInSeconds = expiryInMinutes * 60;
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(restSecurityProps.getCookieProperties().isSecure());
        cookie.setPath(restSecurityProps.getCookieProperties().getPath());
        cookie.setDomain(restSecurityProps.getCookieProperties().getDomain());
        cookie.setMaxAge(expiryInSeconds);
        httpServletResponse.addCookie(cookie);
    }

    public void setSecureCookie(String name, String value, int expiryInMinutes) {
        int expiryInSeconds = expiryInMinutes * 60;
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(restSecurityProps.getCookieProperties().isHttpOnly());
        cookie.setSecure(restSecurityProps.getCookieProperties().isSecure());
        cookie.setPath(restSecurityProps.getCookieProperties().getPath());
        cookie.setDomain(restSecurityProps.getCookieProperties().getDomain());
        cookie.setMaxAge(expiryInSeconds);
        httpServletResponse.addCookie(cookie);
    }

    public void setSecureCookie(String name, String value) {
        int expiryInMinutes = restSecurityProps.getCookieProperties().getMaxAgeInMinutes();
        setSecureCookie(name, value, expiryInMinutes);
    }

    public void deleteSecureCookie(String name) {
        setSecureCookie(name, null, 0);
    }

    public void deleteCookie(String name) {
        setCookie(name, null, 0);
    }
}
