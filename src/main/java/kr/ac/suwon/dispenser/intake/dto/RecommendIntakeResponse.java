package kr.ac.suwon.dispenser.intake.dto;

public record RecommendIntakeResponse(
        Double zinc,
        Double melatonin,
        Double magnesium,
        Double electrolyte
) {
}
