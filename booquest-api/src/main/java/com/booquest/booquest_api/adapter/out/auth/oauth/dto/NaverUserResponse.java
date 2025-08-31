package com.booquest.booquest_api.adapter.out.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverUserResponse {
    @JsonProperty("resultcode")
    private String resultCode;
    
    private String message;
    
    private Response response;
    
    @Getter
    @Setter
    public static class Response {
        private String id;
        private String nickname;
        private String email;
        @JsonProperty("profile_image")
        private String profileImage;
    }
}