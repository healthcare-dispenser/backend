package kr.ac.suwon.dispenser.dispenser.controller;

import kr.ac.suwon.dispenser.dispenser.service.DispenserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispensers")
public class DispenserController {

    private final DispenserService dispenserService;

    
}
