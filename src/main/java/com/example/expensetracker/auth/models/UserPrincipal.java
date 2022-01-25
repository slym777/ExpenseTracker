package com.example.expensetracker.auth.models;

import java.io.Serializable;

public class UserPrincipal implements Serializable {
    private static final long serialVersionUID = 691342L;

    private String uid;
    private String name;
    private String email;
    private boolean isEmailVerified;
    private String issuer;
    private String picture;

    public UserPrincipal() {
    }

    public UserPrincipal(String uid, String name, String email, boolean isEmailVerified, String issuer, String picture) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.issuer = issuer;
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
