package kr.ac.suwon.dispenser.account.controller;

import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.dto.LoginRequest;
import kr.ac.suwon.dispenser.account.dto.SignupRequest;
import kr.ac.suwon.dispenser.account.dto.AuthResponse;
import kr.ac.suwon.dispenser.account.service.AccountService;
import kr.ac.suwon.dispenser.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        Long accountId = accountService.createAccount(request.email(), request.password(), request.passwordConfirm());
        String token = jwtTokenProvider.createAccessToken(accountId);

        URI location = URI.create("/api/accounts/" + accountId);
        return ResponseEntity.created(location).body(new AuthResponse(accountId,request.email(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Account account = accountService.login(request.email(), request.password());
        String token = jwtTokenProvider.createAccessToken(account.getId());
        return ResponseEntity.ok(new AuthResponse(account.getId(), account.getEmail(), token));
    }
}
