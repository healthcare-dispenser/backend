package kr.ac.suwon.dispenser.intake.dto.feedback;

import java.util.List;

public record FeedbackListResponse(
        List<FeedbackResponse> items,
        int count
) {
}
