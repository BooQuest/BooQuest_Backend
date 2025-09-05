package com.booquest.booquest_api.adapter.in.missionstep.dto;

import com.booquest.booquest_api.adapter.in.missionstep.dto.RegenerateMissionStepRequest.RegenerateFeedbackData;

public record RegenerateMissionStepAIRequest (RegenerateFeedbackData feedbackData, MissionStepAiRequestDto generateMissionStep){
}
