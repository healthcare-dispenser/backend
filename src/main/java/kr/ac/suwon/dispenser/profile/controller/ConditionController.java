package kr.ac.suwon.dispenser.profile.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.profile.dto.ConditionItem;
import kr.ac.suwon.dispenser.profile.dto.ConditionListResponse;
import kr.ac.suwon.dispenser.profile.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conditions")
public class ConditionController {

    private final ConditionService conditionService;

    @GetMapping
    public ResponseEntity<ConditionListResponse> getConditions(
            @AuthenticationPrincipal AccountPrincipal account) {
        List<ConditionItem> list = conditionService.findAll().stream().map(c -> new ConditionItem(c.getId(), c.getCode(), c.getLabel())).toList();
        return ResponseEntity.ok(new ConditionListResponse(list, list.size()));
    }
}
