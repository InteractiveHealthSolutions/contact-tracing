package com.ihs.android.contracttracing.contracttracing.models.response.login;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */

public class LoginResponse {
    private String sessionId;
    private Boolean authenticated;
    private User user;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
