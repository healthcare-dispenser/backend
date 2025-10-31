package kr.ac.suwon.dispenser.intake.dto.csv;

import java.time.LocalDateTime;
import kr.ac.suwon.dispenser.intake.domain.IntakeStatus;

public record Csv(
        Long intakeId,
        Long profileId,
        String commandUuid,
        IntakeStatus status,
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        Double zinc,
        Double melatonin,
        Double magnesium,
        Double electrolyte,
        Long feedbackId,
        Integer sleepRating,
        Integer fatigueRating,
        LocalDateTime feedbackCreatedAt
) {}
