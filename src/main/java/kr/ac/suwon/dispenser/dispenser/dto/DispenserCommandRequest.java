package kr.ac.suwon.dispenser.dispenser.dto;

public record DispenserCommandRequest(
        String commandUuid,
        Double zinc,
        Double melatonin,
        Double magnesium,
        Double electrolyte
) {
}
