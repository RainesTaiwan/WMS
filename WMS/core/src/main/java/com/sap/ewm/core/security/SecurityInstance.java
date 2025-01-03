package com.sap.ewm.core.security;

import java.io.Serializable;

/**
 * @program: mesext
 * @description:
 * @author: syngna
 * @create: 2020-06-16 19:50
 */
public class SecurityInstance implements Serializable {

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}