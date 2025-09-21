package kr.ac.suwon.dispenser.account.dto;

public record SignupRequest(
        String email,
        String password,
        String passwordConfirm
) {
}
