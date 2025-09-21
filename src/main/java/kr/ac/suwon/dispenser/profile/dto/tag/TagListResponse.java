package kr.ac.suwon.dispenser.profile.dto.tag;

import java.util.List;

public record TagListResponse(
        List<TagItem> items,
        int count
) {
}
