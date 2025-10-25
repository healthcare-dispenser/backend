package kr.ac.suwon.dispenser.profile.service;


import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.service.AccountService;
import kr.ac.suwon.dispenser.profile.domain.Gender;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.domain.condition.Condition;
import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.domain.tag.Tag;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;
import kr.ac.suwon.dispenser.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountService accountService;
    private final ConditionService conditionService;
    private final TagService tagService;

    public Long createProfile(Long accountId, String name, Integer age, Double height, Double weight, Gender gender,
                              Set<TagCode> tags, Set<ConditionCode> conditions) {
        Account account = accountService.findById(accountId);
        Profile profile = Profile.create(account, name, age, height, weight, gender);
        addTagsAndConditions(tags, conditions, profile);

        return profileRepository.save(profile).getId();
    }

    public List<Profile> getProfiles(Long accountId) {
        return profileRepository.findAllByAccount_Id(accountId);
    }

    public void deleteProfile(Long profileId, Long accountId) {
        int row = profileRepository.deleteByIdAndAccount_Id(profileId, accountId);
        if (row == 0) {
            throw new RuntimeException("프로필을 찾을 수 없습니다.");
        }
    }

    public void updateProfile(Long accountId, Long profileId, String name, Double height, Double weight, Gender gender,
                              Set<TagCode> tags, Set<ConditionCode> conditions) {

        log.info("[ProfileService] 프로필 업데이트 요청 accountId=[{}], profileId=[{}]", accountId, profileId);
        Profile profile = findByIdAndAccountId(profileId, accountId);
        profile.updateProfile(name,height,weight,gender);



        addTagsAndConditions(tags, conditions, profile);
    }

    public Profile findByIdAndAccountId(Long profileId, Long accountId) {
        return profileRepository.findByIdAndAccount_Id(profileId, accountId).orElseThrow(() -> new RuntimeException("존재하지 않는 프로필 입니다."));
    }

    private void addTagsAndConditions(Set<TagCode> tags, Set<ConditionCode> conditions, Profile profile) {

        // null-safe
        Set<TagCode> targetTags = (tags == null) ? Set.of() : tags;
        Set<ConditionCode> targetConds = (conditions == null) ? Set.of() : conditions;

        // TAGS: 제거 - 추가
        // 현재 보유 코드
        Set<TagCode> currentTagCodes = profile.getTags().stream()
                .map(pt -> pt.getTag().getCode())
                .collect(Collectors.toSet());

        // 제거: 요청에 없는 링크 제거 (orphanRemoval=true면 DB에서 DELETE)
        profile.getTags().removeIf(pt -> !targetTags.contains(pt.getTag().getCode()));

        // 추가: target - current 만 추가
        for (TagCode code : targetTags) {
            if (!currentTagCodes.contains(code)) {
                Tag tag = tagService.findByCode(code);
                profile.addTag(tag);
            }
        }

        // CONDITIONS: 제거 - 추가
        Set<ConditionCode> currentCondCodes = profile.getConditions().stream()
                .map(pc -> pc.getCondition().getCode())
                .collect(Collectors.toSet());

        profile.getConditions().removeIf(pc -> !targetConds.contains(pc.getCondition().getCode()));

        for (ConditionCode code : targetConds) {
            if (!currentCondCodes.contains(code)) {
                Condition cond = conditionService.findByCode(code);
                profile.addCondition(cond);
            }
        }
//        Set<Tag> tagSet = tags.stream().map(tagService::findByCode).collect(Collectors.toSet());
//        Set<Condition> conditionSet = conditions.stream().map(conditionService::findByCode).collect(Collectors.toSet());
//
//        tagSet.forEach(profile::addTag);
//        conditionSet.forEach(profile::addCondition);
    }

    public Profile findById(Long profileId) {
        return profileRepository.findById(profileId).orElseThrow(() -> new RuntimeException("존재하지 않는 프로필 입니다."));
    }

}
