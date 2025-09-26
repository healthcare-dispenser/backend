package kr.ac.suwon.dispenser.profile.domain.condition;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_conditions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileCondition {

    @EmbeddedId
    private ProfileConditionId id = new ProfileConditionId();

    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @MapsId("conditionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    private Condition condition;

    @Builder(access = AccessLevel.PRIVATE)
    public ProfileCondition(Profile profile, Condition condition) {
        this.profile = profile;
        this.condition = condition;
    }

    public static ProfileCondition create(Profile profile, Condition condition) {
        return ProfileCondition.builder()
                .profile(profile)
                .condition(condition).build();
    }
}
