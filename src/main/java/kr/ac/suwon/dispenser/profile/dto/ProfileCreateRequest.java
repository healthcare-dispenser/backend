package kr.ac.suwon.dispenser.profile.dto;

import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;

import java.util.Set;

public record ProfileCreateRequest(
        String name,
        Double height,
        Double weight,
        Set<TagCode> tags,
        Set<ConditionCode> conditions
) {
}
