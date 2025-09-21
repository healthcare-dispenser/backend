package kr.ac.suwon.dispenser.profile.dto.tag;

import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;

public record TagItem(
        Long id,
        TagCode code,
        String label
) {
}
