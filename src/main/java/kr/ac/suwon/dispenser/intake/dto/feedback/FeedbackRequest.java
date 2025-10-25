package kr.ac.suwon.dispenser.intake.dto.feedback;

public record FeedbackRequest(
        int sleepQuality,
        int fatigueLevel
) {
}
