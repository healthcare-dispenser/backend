package kr.ac.suwon.dispenser.intake.dto.feedback;

import java.time.LocalDateTime;

public record FeedbackInfo(
        int sleepRating,
        int fatigueRating,
        LocalDateTime latestAt
) {

}
