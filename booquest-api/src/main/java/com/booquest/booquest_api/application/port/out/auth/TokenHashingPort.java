package com.booquest.booquest_api.application.port.out.auth;

public interface TokenHashingPort {
    String sha256Base64(String input);
}
