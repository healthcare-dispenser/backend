package kr.ac.suwon.dispenser.account.dto;

public record LoginRequest(
        String email,
        String password
) {
}
