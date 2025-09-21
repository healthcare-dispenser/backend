package kr.ac.suwon.dispenser.profile.dto.condition;

import java.util.List;

public record ConditionListResponse(
        List<ConditionItem> items,
        int size
) {
}
