package kr.ac.suwon.dispenser.profile.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.profile.dto.tag.TagItem;
import kr.ac.suwon.dispenser.profile.dto.tag.TagListResponse;
import kr.ac.suwon.dispenser.profile.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<TagListResponse> getTags(
            @AuthenticationPrincipal AccountPrincipal account) {
        List<TagItem> list = tagService.findAll().stream().map(t -> new TagItem(t.getId(), t.getCode(), t.getLabel())).toList();
        return ResponseEntity.ok(new TagListResponse(list, list.size()));
    }
}
