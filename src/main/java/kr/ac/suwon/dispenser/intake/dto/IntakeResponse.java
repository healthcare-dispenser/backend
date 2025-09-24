package kr.ac.suwon.dispenser.intake.dto;

import kr.ac.suwon.dispenser.intake.domain.IntakeStatus;

public record IntakeResponse(
        Long intakeId,
        IntakeStatus status
) {
}
