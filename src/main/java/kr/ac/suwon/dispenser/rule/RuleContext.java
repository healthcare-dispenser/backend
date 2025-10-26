package kr.ac.suwon.dispenser.rule;

import kr.ac.suwon.dispenser.profile.domain.Gender;
import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;

import java.util.Set;

public record RuleContext(
        Integer age,
        Double height,
        Double weight,
        Gender gender,
        Set<TagCode> tags,
        Set<ConditionCode> conditions
) {
}
