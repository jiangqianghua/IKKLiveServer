package com.jiang.im.im;

import javax.websocket.Session;

public class UserSession {

    private Session session ;

    private String customParam ;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getCustomParam() {
        return customParam;
    }

    public void setCustomParam(String customParam) {
        this.customParam = customParam;
    }
}
