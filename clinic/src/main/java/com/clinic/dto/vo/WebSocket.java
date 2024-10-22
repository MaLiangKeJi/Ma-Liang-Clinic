package com.clinic.dto.vo;

import javax.websocket.Session;

public class WebSocket {
    /**
     * session
     */
    private Session session;
    /**
     * 账号id
     */
    private Long userId;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}