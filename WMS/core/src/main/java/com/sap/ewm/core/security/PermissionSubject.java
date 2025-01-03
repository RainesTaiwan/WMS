package com.sap.ewm.core.security;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-06-28 15:30
 */
public class PermissionSubject {

    private List<PermissionListener> listeners = new LinkedList<>();

    private static PermissionSubject instance = new PermissionSubject();

    private PermissionSubject() {

    }

    public void registerListener(PermissionListener observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        if (!this.listeners.contains(observer)) {
            this.listeners.add(observer);
        }
    }

    public synchronized void removeListener(PermissionListener observer) {
        if(!this.listeners.isEmpty()) {
            this.listeners.remove(observer);
        }
    }

    public boolean hasPermission(String user, List<String> permissionList) {
        for(PermissionListener listener : this.listeners) {
            if(!listener.hasPermission(user, permissionList)) {
                return false;
            }
        }
        return true;
    }

    public static PermissionSubject getInstance() {
        return instance;
    }

    public static void setInstance(PermissionSubject instance) {
        PermissionSubject.instance = instance;
    }
}