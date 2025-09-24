package kr.ac.suwon.dispenser.intake.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.intake.dto.IntakeRequest;
import kr.ac.suwon.dispenser.intake.dto.IntakeResponse;
import kr.ac.suwon.dispenser.intake.service.IntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/intakes")
public class IntakeController {

    private final IntakeService intakeService;

    @PostMapping
    public ResponseEntity<IntakeResponse> commandDispense(@AuthenticationPrincipal AccountPrincipal account,
                                                          @RequestBody IntakeRequest request) {
        Long intakeId = intakeService.recordIntake(request.profileId(), request.dispenserUuid());
        return ResponseEntity.accepted().body(new IntakeResponse(intakeId));
    }

    @GetMapping("/{intakeId}")
    public ResponseEntity<?> getCommandStatus(@AuthenticationPrincipal AccountPrincipal account) {

    }
}
