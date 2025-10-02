package kr.ac.suwon.dispenser.profile.domain;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.profile.domain.condition.Condition;
import kr.ac.suwon.dispenser.profile.domain.condition.ProfileCondition;
import kr.ac.suwon.dispenser.profile.domain.tag.ProfileTag;
import kr.ac.suwon.dispenser.profile.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfileTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfileCondition> conditions = new HashSet<>();

    private String name;
    private Integer age;
    private Double height;
    private Double weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder(access = AccessLevel.PRIVATE)
    public Profile(Account account, String name, Integer age, Double height, Double weight, Gender gender) {
        this.account = account;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public static Profile create(Account account, String name, Integer age, Double height, Double weight, Gender gender) {
        return Profile.builder()
                .account(account)
                .name(name)
                .age(age)
                .height(height)
                .weight(weight)
                .gender(gender).build();
    }

    public void addTag(Tag tag) {
        ProfileTag profileTag = ProfileTag.create(this, tag);
        tags.add(profileTag);
    }

    public void addCondition(Condition condition) {
        ProfileCondition profileCondition = ProfileCondition.create(this, condition);
        conditions.add(profileCondition);
    }

    public void updateProfile(String name, Double height, Double weight, Gender gender) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }


}
