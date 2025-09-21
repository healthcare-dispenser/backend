package kr.ac.suwon.dispenser.profile.dto.condition;

import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;

public record ConditionItem(
        Long id,
        ConditionCode code,
        String label
) {
}
