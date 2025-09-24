package kr.ac.suwon.dispenser.intake.dto;

public record IntakeRequest(
        Long profileId,
        String dispenserUuid
) {
}
