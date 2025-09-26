package kr.ac.suwon.dispenser.profile.domain.condition;


import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ProfileConditionId implements Serializable {
    private Long profileId;
    private Long conditionId;

}

