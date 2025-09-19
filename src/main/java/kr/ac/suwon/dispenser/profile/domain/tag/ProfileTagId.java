package kr.ac.suwon.dispenser.profile.domain.tag;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileTagId implements Serializable {
    private Long profileId;
    private Long tagId;

}
