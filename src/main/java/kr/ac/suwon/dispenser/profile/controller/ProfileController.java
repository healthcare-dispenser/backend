package kr.ac.suwon.dispenser.profile.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;
import kr.ac.suwon.dispenser.profile.dto.ProfileCreateRequest;
import kr.ac.suwon.dispenser.profile.dto.ProfileInfo;
import kr.ac.suwon.dispenser.profile.dto.ProfileItem;
import kr.ac.suwon.dispenser.profile.dto.ProfileListResponse;
import kr.ac.suwon.dispenser.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileListResponse> getProfiles(@AuthenticationPrincipal AccountPrincipal account) {
        List<Profile> profiles = profileService.getProfiles(account.getId());
        List<ProfileItem> list = profiles.stream().map(p -> new ProfileItem(p.getId(), p.getName())).toList();
        return ResponseEntity.ok(new ProfileListResponse(list, list.size()));
    }

    @PostMapping
    public ResponseEntity<ProfileItem> createProfile(
            @AuthenticationPrincipal AccountPrincipal account,
            @RequestBody ProfileCreateRequest request) {
        Long profileId = profileService.createProfile(account.getId(), request.name(), request.height(), request.weight(),
                request.gender(), request.tags(), request.conditions());

        URI location = URI.create("api/profiles/" + profileId);
        return ResponseEntity.created(location).body(new ProfileItem(profileId, request.name()));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @AuthenticationPrincipal AccountPrincipal account,
            @PathVariable Long profileId) {
        profileService.deleteProfile(profileId, account.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{profileId}")
    public ResponseEntity<ProfileItem> updateProfile(
            @AuthenticationPrincipal AccountPrincipal account,
            @PathVariable Long profileId,
            @RequestBody ProfileCreateRequest request) {
        profileService.updateProfile(account.getId(), profileId, request.name(), request.height(), request.weight(),
                request.gender(), request.tags(), request.conditions());
        return ResponseEntity.ok(new ProfileItem(profileId, request.name()));
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileInfo> getProfile(
            @AuthenticationPrincipal AccountPrincipal account,
            @PathVariable Long profileId) {
        Profile profile = profileService.findByIdAndAccountId(profileId, account.getId());
        Set<TagCode> tags = profile.getTags().stream().map(pt -> pt.getTag().getCode()).collect(Collectors.toSet());
        Set<ConditionCode> conditions = profile.getConditions().stream().map(pc -> pc.getCondition().getCode()).collect(Collectors.toSet());
        return ResponseEntity.ok(new ProfileInfo(profileId, profile.getName(), profile.getHeight(), profile.getWeight(),
                profile.getGender(), tags, conditions));
    }
}
