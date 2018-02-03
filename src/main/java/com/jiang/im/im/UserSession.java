package com.jiang.im.im;

import javax.websocket.Session;

public class UserSession {

    private Session session ;

    private String userId ;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
