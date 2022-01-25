package com.example.expensetracker.auth.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("security")
public class SecurityProperties {
    private CookieProperties cookieProperties;
    private FirebaseProperties firebaseProperties;
    private boolean allowCredentials;
    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private List<String> allowedMethods;
    private List<String> allowedPublicApis;

    public SecurityProperties() {
    }

    public CookieProperties getCookieProperties() {
        return cookieProperties;
    }

    public void setCookieProperties(CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    public FirebaseProperties getFirebaseProperties() {
        return firebaseProperties;
    }

    public void setFirebaseProperties(FirebaseProperties firebaseProperties) {
        this.firebaseProperties = firebaseProperties;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedPublicApis() {
        return allowedPublicApis;
    }

    public void setAllowedPublicApis(List<String> allowedPublicApis) {
        this.allowedPublicApis = allowedPublicApis;
    }
}
