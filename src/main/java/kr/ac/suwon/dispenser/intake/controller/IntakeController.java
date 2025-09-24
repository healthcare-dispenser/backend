package kr.ac.suwon.dispenser.intake.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.intake.domain.Intake;
import kr.ac.suwon.dispenser.intake.dto.IntakeRequest;
import kr.ac.suwon.dispenser.intake.dto.IntakeResponse;
import kr.ac.suwon.dispenser.intake.service.IntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/intakes")
public class IntakeController {

    private final IntakeService intakeService;

    @PostMapping
    public ResponseEntity<IntakeResponse> commandDispense(@AuthenticationPrincipal AccountPrincipal account,
                                                          @RequestBody IntakeRequest request) {
        Intake intake = intakeService.recordIntake(request.profileId(), request.dispenserUuid());
        URI location = URI.create("/api/intakes/" + intake.getId());
        return ResponseEntity.accepted().location(location).body(new IntakeResponse(intake.getId(), intake.getStatus()));
    }

    @GetMapping("/{intakeId}")
    public ResponseEntity<IntakeResponse> getCommandStatus(@AuthenticationPrincipal AccountPrincipal account,
                                                         @PathVariable Long intakeId) {
        Intake intake = intakeService.findById(intakeId);
        return ResponseEntity.ok().body(new IntakeResponse(intakeId, intake.getStatus()));
    }
}
