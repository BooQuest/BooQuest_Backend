package com.booquest.booquest_api.application.port.in.dto;

import java.util.List;

public record GenerateSideJobRequest(long userId, String job, List<String> hobbies, String expressionStyle,
                                     String strengthType, String desiredSideJob) {}
