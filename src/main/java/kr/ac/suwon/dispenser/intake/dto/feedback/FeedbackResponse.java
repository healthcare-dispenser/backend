package kr.ac.suwon.dispenser.intake.dto.feedback;

import java.time.LocalDateTime;

public record FeedbackResponse(
        Long feedbackId,
        int sleepQuality,
        int fatigueLevel,
        LocalDateTime recordDate
) {

}
