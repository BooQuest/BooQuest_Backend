package com.booquest.booquest_api.application.service.chat;

import com.booquest.booquest_api.adapter.in.chat.web.dto.*;
import com.booquest.booquest_api.application.port.in.chat.ChatUseCases;
import com.booquest.booquest_api.application.port.out.chat.ChatConversationRepositoryPort;
import com.booquest.booquest_api.application.port.out.chat.ChatMessageRepositoryPort;
import com.booquest.booquest_api.domain.chat.model.ChatConversation;
import com.booquest.booquest_api.domain.chat.model.ChatMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService implements ChatUseCases {

    private static final int DAILY_CHAT_LIMIT = 10;
    private static final String USER_ROLE = "user";

    private final ChatConversationRepositoryPort conversationRepositoryPort;
    private final ChatMessageRepositoryPort messageRepositoryPort;
    private final @Qualifier("aiWebClient") WebClient aiWebClient;

    @Override
    @Transactional
    public ChatSendResponse sendMessage(Long userId, ChatSendCommand command) {
        boolean isOverLimit = isOverDailyUsageLimit(userId);

        if (isOverLimit) {
            return ChatSendResponse.builder()
                    .conversationId(null)
                    .message("챗봇 대화는 하루 10회까지 가능합니다.")
                    .build();
        }

        ChatConversation conversation = ensureConversation(userId, command.getConversationId(), command.getMessage());

        // save user message
        ChatMessage userMsg = ChatMessage.builder()
                .conversation(conversation)
                .role("user")
                .content(command.getMessage())
                .build();
        messageRepositoryPort.save(userMsg);

        // call AI
        AiChatRequest req = new AiChatRequest(userId, command.getMessage());
        AiChatResponse aiRes = aiWebClient.post()
                .uri("/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
//                .retrieve()
//                .bodyToMono(AiChatResponse.class)
                .exchangeToMono(res -> {
                    if (res.statusCode().is2xxSuccessful()) return res.bodyToMono(AiChatResponse.class);
                    return res.bodyToMono(String.class).defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("AI POST /ai/chat -> {} body={}", res.statusCode(), body);
                                return Mono.error(new RuntimeException("AI error " + res.statusCode() + " : " + body));
                            });
                })
                .block();

        String assistantMessage = aiRes != null ? aiRes.getMessage() : "";

        // save assistant message
        ChatMessage assistantMsg = ChatMessage.builder()
                .conversation(conversation)
                .role("assistant")
                .content(assistantMessage)
                .build();
        messageRepositoryPort.save(assistantMsg);

        // update title if empty - call AI for title summarization
        if (conversation.getTitle() == null || conversation.getTitle().isBlank()) {
            String summarizedTitle = generateTitleFromAI(command.getMessage());
            conversation.updateTitleIfEmpty(summarizedTitle);
            conversationRepositoryPort.save(conversation);
        }

        return ChatSendResponse.builder()
                .conversationId(conversation.getId())
                .message(assistantMessage)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ChatListResponse getConversations(Long userId, YearMonth month) {
        List<ChatConversation> list;
        if (month != null) {
            LocalDateTime start = month.atDay(1).atStartOfDay();
            LocalDate endDay = month.atEndOfMonth();
            LocalDateTime end = endDay.atTime(23, 59, 59);
            list = conversationRepositoryPort.findByUserIdAndCreatedAtBetween(userId, start, end);
        } else {
            list = conversationRepositoryPort.findByUserIdOrderByUpdatedAtDesc(userId);
        }

        Map<YearMonth, List<ChatConversation>> grouped = list.stream()
                .collect(Collectors.groupingBy(c -> YearMonth.from(c.getCreatedAt().toLocalDate())));

        List<ChatListGroup> groups = grouped.entrySet().stream()
                .sorted(Map.Entry.<YearMonth, List<ChatConversation>>comparingByKey().reversed())
                .map(e -> ChatListGroup.builder()
                        .month(e.getKey())
                        .items(e.getValue().stream()
                                .sorted(Comparator.comparing(ChatConversation::getCreatedAt).reversed())
                                .map(c -> ChatListItem.builder()
                                        .conversationId(c.getId())
                                        .title(c.getTitle())
                                        .createdAt(c.getCreatedAt())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        return ChatListResponse.builder().groups(groups).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ChatMessageListResponse getConversation(Long userId, UUID conversationId) {
        ChatConversation conv = conversationRepositoryPort.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        if (!conv.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Forbidden");
        }

        List<ChatMessage> messages = messageRepositoryPort.findByConversationIdOrderByIdAsc(conversationId);
        List<ChatMessageItem> items = messages.stream()
                .map(m -> ChatMessageItem.builder()
                        .id(m.getId())
                        .role(m.getRole())
                        .content(m.getContent())
                        .createdAt(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ChatMessageListResponse.builder()
                .conversationId(conv.getId())
                .title(conv.getTitle())
                .messages(items)
                .build();
    }

    private boolean isOverDailyUsageLimit(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime nextDayStart = today.plusDays(1).atStartOfDay();

        long usedCount = messageRepositoryPort.countUserMessagesForUserBetween(
                userId,
                USER_ROLE,
                startOfDay,
                nextDayStart
        );

        return usedCount >= DAILY_CHAT_LIMIT;
    }

//    private ChatConversation ensureConversation(Long userId, UUID conversationId, String firstMessage) {
//        if (conversationId != null) {
//            ChatConversation conv = conversationRepositoryPort.findById(conversationId)
//                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
//            if (!conv.getUserId().equals(userId)) {
//                throw new IllegalArgumentException("Forbidden");
//            }
//            return conv;
//        }
//        return conversationRepositoryPort.save(ChatConversation.builder()
//                .userId(userId)
//                .title(truncateTitle(firstMessage))
//                .build());
//    }

    private ChatConversation ensureConversation(Long userId, UUID conversationId, String firstMessage) {
        // 1) 기존 대화로 보내는 경우: id + user 동시검증
        if (conversationId != null) {
            return conversationRepositoryPort.findByIdAndUserId(conversationId, userId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        }

        // 2) 새 대화 생성: AI로 제목 요약 생성
        String summarizedTitle = generateTitleFromAI(firstMessage);
        ChatConversation conv = ChatConversation.builder()
                .userId(userId)
                .title(summarizedTitle)
                .build();
        return conversationRepositoryPort.save(conv);
    }

    private String generateTitleFromAI(String message) {
        if (message == null || message.isBlank()) {
            return "새 대화";
        }
        
        try {
            AiTitleRequest req = new AiTitleRequest(message);
            AiTitleResponse aiRes = aiWebClient.post()
                    .uri("/ai/title/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(req)
                    .exchangeToMono(res -> {
                        if (res.statusCode().is2xxSuccessful()) return res.bodyToMono(AiTitleResponse.class);
                        return res.bodyToMono(String.class).defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.error("AI POST /ai/title/summarize -> {} body={}", res.statusCode(), body);
                                    return Mono.error(new RuntimeException("AI title error " + res.statusCode() + " : " + body));
                                });
                    })
                    .block();
            
            return aiRes != null && aiRes.getTitle() != null && !aiRes.getTitle().isBlank() 
                ? aiRes.getTitle() 
                : truncateTitle(message);
        } catch (Exception e) {
            log.warn("AI title generation failed, using truncated message: {}", e.getMessage());
            return truncateTitle(message);
        }
    }
    
    private String truncateTitle(String message) {
        if (message == null) return "새 대화";
        String trimmed = message.trim();
        return trimmed.length() > 30 ? trimmed.substring(0, 30) + "..." : trimmed;
    }

    // Internal DTOs for AI service
    @Getter
    @AllArgsConstructor
    static class AiChatRequest {
        private Long user_id;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    static class AiChatResponse {
        private String message;
    }
    
    @Getter
    @AllArgsConstructor
    static class AiTitleRequest {
        private String message;
    }
    
    @Getter
    @NoArgsConstructor
    static class AiTitleResponse {
        private String title;
    }
}
