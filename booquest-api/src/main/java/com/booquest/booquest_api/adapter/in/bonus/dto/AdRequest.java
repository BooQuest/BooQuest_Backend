package com.booquest.booquest_api.adapter.in.bonus.dto;

import lombok.Data;

@Data
public class AdRequest {
    private String receipt;
    private String adSessionId; // receipt, adSessionId 둘 중 하나로 결정하고 다른 필드는 삭제
}
