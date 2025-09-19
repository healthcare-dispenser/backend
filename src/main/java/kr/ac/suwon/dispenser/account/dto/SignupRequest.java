package kr.ac.suwon.dispenser.account.dto;

public record SignupRequest(
        String name,
        String email,
        String password
) {
}
