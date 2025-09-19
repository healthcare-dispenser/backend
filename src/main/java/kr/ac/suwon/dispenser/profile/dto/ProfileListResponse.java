package kr.ac.suwon.dispenser.profile.dto;

import java.util.List;

public record ProfileListResponse(
        List<ProfileItem> items,
        int count

) {
}
