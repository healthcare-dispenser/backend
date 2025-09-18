package kr.ac.suwon.dispenser.profile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileTag {

    @EmbeddedId
    private ProfileTagId id;

    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;


    @Builder(access = AccessLevel.PRIVATE)
    public ProfileTag(Profile profile, Tag tag) {
        this.profile = profile;
        this.tag = tag;
    }

    public static ProfileTag create(Profile profile, Tag tag) {
        return ProfileTag.builder()
                .profile(profile)
                .tag(tag).build();

    }
}
