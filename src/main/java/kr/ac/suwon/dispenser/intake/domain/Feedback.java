package kr.ac.suwon.dispenser.intake.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedbacks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private int sleepRating;
    private int fatigueRating;
    private LocalDateTime createdAt;

    public static Feedback create(Profile profile, int sleepRating, int fatigueRating) {

        Feedback feedback = Feedback.builder()
                .profile(profile)
                .sleepRating(sleepRating)
                .fatigueRating(fatigueRating).build();

        feedback.createdAt = LocalDateTime.now();
        return feedback;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public Feedback(Profile profile, int sleepRating, int fatigueRating, LocalDateTime createdAt) {
        this.profile = profile;
        this.sleepRating = sleepRating;
        this.fatigueRating = fatigueRating;
        this.createdAt = createdAt;
    }

}
