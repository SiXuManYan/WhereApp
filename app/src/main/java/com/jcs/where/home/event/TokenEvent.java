package com.jcs.where.home.event;

public class TokenEvent {
    private String token;

    public TokenEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
