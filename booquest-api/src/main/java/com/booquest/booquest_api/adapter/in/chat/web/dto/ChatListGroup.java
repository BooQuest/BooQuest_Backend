package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ChatListGroup {
    private YearMonth month;
    private List<ChatListItem> items;
}
