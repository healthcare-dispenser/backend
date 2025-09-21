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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountService accountService;
    private final ConditionService conditionService;
    private final TagService tagService;

    public Long createProfile(Long accountId, String name, Double height, Double weight, Gender gender,
                              Set<TagCode> tags, Set<ConditionCode> conditions) {
        Account account = accountService.findById(accountId);
        Profile profile = Profile.create(account, name, height, weight, gender);

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
        Profile profile = findByIdAndAccountId(accountId, profileId);
        profile.updateProfile(name,height,weight,gender);

        addTagsAndConditions(tags, conditions, profile);
    }

    public Profile findByIdAndAccountId(Long profileId, Long accountId) {
        return profileRepository.findByIdAndAccount_Id(profileId, accountId).orElseThrow(() -> new RuntimeException("존재하지 않는 프로필 입니다."));
    }

    private void addTagsAndConditions(Set<TagCode> tags, Set<ConditionCode> conditions, Profile profile) {
        Set<Tag> tagSet = tags.stream().map(tagService::findByCode).collect(Collectors.toSet());
        Set<Condition> conditionSet = conditions.stream().map(conditionService::findByCode).collect(Collectors.toSet());

        tagSet.forEach(profile::addTag);
        conditionSet.forEach(profile::addCondition);
    }
}
