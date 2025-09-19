package kr.ac.suwon.dispenser.account.dto;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String token
) {
}
