package com.booquest.booquest_api.application.port.in.chat;

public record ChatDeleteResult(
        long deletedMessages,
        long deletedConversations
) {}
