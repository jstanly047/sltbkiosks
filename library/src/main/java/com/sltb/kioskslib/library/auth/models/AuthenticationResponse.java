package com.sltb.kioskslib.library.auth.models;

public class AuthenticationResponse {

    private final String jwt;
    private final String description;
    private final Status authenticationStatus;

    public AuthenticationResponse(String jwt, Status authenticationStatus, String description  ) {
        this.jwt = jwt;
        this.authenticationStatus = authenticationStatus;
        this.description = description;
    }

    public String getJwt() {
        return jwt;
    }
}
