package kr.ac.suwon.dispenser.intake.controller;

import java.util.Map;
import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.intake.domain.Intake;
import kr.ac.suwon.dispenser.intake.dto.IntakeItem;
import kr.ac.suwon.dispenser.intake.dto.IntakeListResponse;
import kr.ac.suwon.dispenser.intake.dto.IntakeRequest;
import kr.ac.suwon.dispenser.intake.dto.IntakeResponse;
import kr.ac.suwon.dispenser.intake.dto.RecommendIntakeResponse;
import kr.ac.suwon.dispenser.intake.service.IntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class IntakeController {

    private final IntakeService intakeService;

    @PostMapping("/api/intakes")
    public ResponseEntity<IntakeResponse> commandDispense(@AuthenticationPrincipal AccountPrincipal account,
                                                          @RequestBody IntakeRequest request) {
        Intake intake = intakeService.recordIntake(request.profileId(), request.dispenserUuid());
        URI location = URI.create("/api/intakes/" + intake.getId());
        return ResponseEntity.accepted().location(location)
                .body(new IntakeResponse(intake.getId(), intake.getStatus()));
    }

    @GetMapping("/api/intakes/{intakeId}")
    public ResponseEntity<IntakeResponse> getCommandStatus(@AuthenticationPrincipal AccountPrincipal account,
                                                           @PathVariable Long intakeId) {
        Intake intake = intakeService.findById(intakeId);
        return ResponseEntity.ok().body(new IntakeResponse(intakeId, intake.getStatus()));
    }

    @GetMapping("/api/profiles/{profileId}/intakes")
    public ResponseEntity<IntakeListResponse> getIntakeList(@AuthenticationPrincipal AccountPrincipal account,
                                                            @PathVariable Long profileId) {
        List<Intake> intakeList = intakeService.findAllByProfileId(profileId);
        List<IntakeItem> list = intakeList.stream().map(i -> new IntakeItem(i.getId(), i.getZinc(), i.getMelatonin(),
                i.getMagnesium(), i.getElectrolyte(), i.getStatus(), i.getProfileSnapshot(),
                i.getRequestedAt(), i.getCompletedAt())).toList();

        return ResponseEntity.ok().body(new IntakeListResponse(list, list.size()));
    }

    @GetMapping("/api/profiles/{profileId}/recommend")
    public ResponseEntity<RecommendIntakeResponse> getRecommend(
            @AuthenticationPrincipal AccountPrincipal account,
            @PathVariable Long profileId) {
        Map<String, Double> plan = intakeService.getPlan(profileId);
        return ResponseEntity.ok(new RecommendIntakeResponse(plan.get("ZINC"),
                plan.get("MELATONIN"), plan.get("MAGNESIUM"), plan.get("ELECTROLYTE")));
    }
}
