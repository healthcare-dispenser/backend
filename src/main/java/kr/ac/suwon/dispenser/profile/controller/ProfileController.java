package kr.ac.suwon.dispenser.profile.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.dto.ProfileCreateRequest;
import kr.ac.suwon.dispenser.profile.dto.ProfileItem;
import kr.ac.suwon.dispenser.profile.dto.ProfileListResponse;
import kr.ac.suwon.dispenser.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
                request.tags(), request.conditions());

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
}
