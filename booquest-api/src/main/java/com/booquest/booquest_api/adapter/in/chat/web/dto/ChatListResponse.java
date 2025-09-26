package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ChatListResponse {
    private List<ChatListGroup> groups; // month-grouped
}
