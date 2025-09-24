package kr.ac.suwon.dispenser.dispenser.controller;

import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserAssignRequest;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserAssignResponse;
import kr.ac.suwon.dispenser.dispenser.service.DispenserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispensers")
public class DispenserController {

    private final DispenserService dispenserService;

    @PostMapping
    public ResponseEntity<DispenserAssignResponse> assignAccount(@AuthenticationPrincipal AccountPrincipal account,
                                                                 @RequestBody DispenserAssignRequest request) {
        dispenserService.assignAccount(account.getId(), request.uuid());
        return ResponseEntity.ok().body(new DispenserAssignResponse(account.getId(), request.uuid()));
    }
    
}
