package kr.ac.suwon.dispenser.intake.repository;

import java.util.List;
import java.util.Optional;
import kr.ac.suwon.dispenser.intake.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByProfile_IdOrderByCreatedAtDesc(Long profileId);

    Optional<Feedback> findTop1ByProfile_IdOrderByCreatedAtDesc(Long profileId);
}
