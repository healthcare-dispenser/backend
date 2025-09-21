package kr.ac.suwon.dispenser.profile.dto;

import java.util.List;

public record TagListResponse(
        List<TagItem> items,
        int count
) {
}
