package kr.ac.suwon.dispenser.intake.dto;

import java.util.List;

public record IntakeListResponse(
        List<IntakeItem> items,
        int count
) {
}
