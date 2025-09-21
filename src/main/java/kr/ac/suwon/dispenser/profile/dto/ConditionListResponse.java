package kr.ac.suwon.dispenser.profile.dto;

import java.util.List;

public record ConditionListResponse(
        List<ConditionItem> items,
        int size
) {
}
