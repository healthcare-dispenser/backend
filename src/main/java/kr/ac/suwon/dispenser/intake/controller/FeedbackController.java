package kr.ac.suwon.dispenser.intake.controller;

import java.net.URI;
import java.util.List;
import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.intake.domain.Feedback;
import kr.ac.suwon.dispenser.intake.dto.feedback.FeedbackListResponse;
import kr.ac.suwon.dispenser.intake.dto.feedback.FeedbackRequest;
import kr.ac.suwon.dispenser.intake.dto.feedback.FeedbackResponse;
import kr.ac.suwon.dispenser.intake.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/api/profiles/{profileId}/feedbacks")
    public ResponseEntity<FeedbackResponse> recordFeedback(@AuthenticationPrincipal AccountPrincipal account,
                                                           @PathVariable Long profileId,
                                                           @RequestBody FeedbackRequest request) {

        Feedback feedback = feedbackService.recordFeedback(profileId, request.sleepQuality(),
                request.fatigueLevel());

        URI location = URI.create("/api/profiles/" + profileId + feedback.getId());
        return ResponseEntity.created(location).body(new FeedbackResponse(feedback.getId(), feedback.getSleepRating(),
                feedback.getFatigueRating(), feedback.getCreatedAt()));
    }

    @GetMapping("/api/profiles/{profileId}/feedbacks")
    public ResponseEntity<FeedbackListResponse> getFeedback(@AuthenticationPrincipal AccountPrincipal account,
                                                            @PathVariable Long profileId) {
        List<Feedback> feedbacks = feedbackService.findAllByProfileId(profileId);
        List<FeedbackResponse> list = feedbacks.stream()
                .map(f -> new FeedbackResponse(f.getId(), f.getSleepRating(), f.getFatigueRating(), f.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(new FeedbackListResponse(list, list.size()));
    }

    @GetMapping("/api/profiles/{profileId}/feedbacks/{feedbackId}")
    public ResponseEntity<FeedbackResponse> getFeedbackList(@AuthenticationPrincipal AccountPrincipal account,
                                                            @PathVariable Long profileId,
                                                            @PathVariable Long feedbackId) {
        Feedback feedback = feedbackService.findById(feedbackId);
        return ResponseEntity.ok(new FeedbackResponse(feedback.getId(), feedback.getSleepRating(),
                feedback.getFatigueRating(), feedback.getCreatedAt()));
    }

}
