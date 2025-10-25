package kr.ac.suwon.dispenser.intake.service;

import java.util.List;
import kr.ac.suwon.dispenser.intake.domain.Feedback;
import kr.ac.suwon.dispenser.intake.repository.FeedbackRepository;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProfileService profileService;

    public Feedback recordFeedback(Long profileId, int sleepRating, int fatigueRating) {
        Profile profile = profileService.findById(profileId);
        return feedbackRepository.save(Feedback.create(profile, sleepRating, fatigueRating));
    }

    public Feedback findById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(() -> new RuntimeException("존재하지 않는 컨디션 기록 입니다."));
    }


    public List<Feedback> findAllByProfileId(Long profileId) {
        return feedbackRepository.findAllByProfile_IdOrderByCreatedAtDesc(profileId);
    }


}
