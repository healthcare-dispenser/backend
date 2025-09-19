package kr.ac.suwon.dispenser.profile.service;


import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.service.AccountService;
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

    public Long createProfile(Long accountId, String name, Double height, Double weight,
                              Set<TagCode> tags, Set<ConditionCode> conditions) {
        Account account = accountService.findById(accountId);
        Profile profile = Profile.create(account, name, height, weight);

        Set<Tag> tagSet = tags.stream().map(tagService::findByCode).collect(Collectors.toSet());
        Set<Condition> conditionSet = conditions.stream().map(conditionService::findByCode).collect(Collectors.toSet());

        tagSet.forEach(profile::addTag);
        conditionSet.forEach(profile::addCondition);

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
}
