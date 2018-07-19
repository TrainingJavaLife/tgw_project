package com.tgw360.entity.websocket;

import java.security.Principal;

/**
 * 安全认证
 */
public class StompPrincipal implements Principal {
    String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
