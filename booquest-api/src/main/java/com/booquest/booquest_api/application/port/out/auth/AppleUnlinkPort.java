package com.booquest.booquest_api.application.port.out.auth;

public interface AppleUnlinkPort {
    boolean revokeToken(String refreshToken);
}
