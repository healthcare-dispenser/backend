package kr.ac.suwon.dispenser;

import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.repository.AccountRepository;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.dispenser.repository.DispenserRepository;
import kr.ac.suwon.dispenser.profile.domain.Gender;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.domain.condition.Condition;
import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.domain.tag.Tag;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;
import kr.ac.suwon.dispenser.profile.repository.ConditionRepository;
import kr.ac.suwon.dispenser.profile.repository.ProfileRepository;
import kr.ac.suwon.dispenser.profile.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final AccountRepository accountRepository;
    private final DispenserRepository dispenserRepository;
    private final ProfileRepository profileRepository;
    private final ConditionRepository conditionRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Account a1 = Account.create("a@a.com", passwordEncoder.encode("123"));
        Account a2 = Account.create("test", passwordEncoder.encode("test"));

        accountRepository.save(a1);
        accountRepository.save(a2);

        Profile p1 = Profile.create(a1, "세현", 24, 180.0, 70.0, Gender.MALE);
        Profile p2 = Profile.create(a1, "다인", 21, 160.0, 40.0, Gender.FEMALE);
        Profile p3 = Profile.create(a1, "다혜", 22,  160.0, 40.0, Gender.FEMALE);
        Profile p4 = Profile.create(a2, "test", 70, 160.0, 40.0, Gender.MALE);

        profileRepository.save(p1);
        profileRepository.save(p2);
        profileRepository.save(p3);
        profileRepository.save(p4);

        Dispenser dispenser = Dispenser.create("TEST-UUID");
        dispenser.assignAccount(a1);

        dispenserRepository.save(dispenser);

        Condition c1 = Condition.create(ConditionCode.PREGNANT, "임산부");
        Condition c2 = Condition.create(ConditionCode.CARDIOVASCULAR, "심혈관질환");
        Condition c3 = Condition.create(ConditionCode.LIVER_DISEASE, "간질환");
        Condition c4 = Condition.create(ConditionCode.KIDNEY_DISEASE, "신장질환");

        conditionRepository.save(c1);
        conditionRepository.save(c2);
        conditionRepository.save(c3);
        conditionRepository.save(c4);

        Tag t1 = Tag.create(TagCode.ALCOHOL, "술을 자주 마셔요");
        Tag t2 = Tag.create(TagCode.VEGAN, "채식위주로 식사해요");
        Tag t3 = Tag.create(TagCode.EXERCISE, "운동/땀 많아요");
        Tag t4 = Tag.create(TagCode.SLEEP, "수면이 불규칙해요");
        Tag t5 = Tag.create(TagCode.CAFFEINE, "카페인을 많이 섭취해요");
        Tag t6 = Tag.create(TagCode.STRESS, "스트레스가 많아요");
        Tag t7 = Tag.create(TagCode.SALTY_FOOD, "짠 음식을 즐겨요");

        tagRepository.save(t1);
        tagRepository.save(t2);
        tagRepository.save(t3);
        tagRepository.save(t4);
        tagRepository.save(t5);
        tagRepository.save(t6);
        tagRepository.save(t7);
    }
}
