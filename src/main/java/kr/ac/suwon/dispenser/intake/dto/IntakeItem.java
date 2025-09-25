package kr.ac.suwon.dispenser.intake.dto;

import kr.ac.suwon.dispenser.intake.domain.IntakeStatus;

import java.time.LocalDateTime;

public record IntakeItem(
        Long intakeId,
        Double vitamin,
        Double melatonin,
        Double magnesium,
        Double electrolyte,
        IntakeStatus status,
        String profileSnapshot,
        LocalDateTime requestedAt,
        LocalDateTime completedAt
) {
}
