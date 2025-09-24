package kr.ac.suwon.dispenser.dispenser.dto;

public record DispenserCommandRequest(
        String commandUuid,
        Double vitamin,
        Double melatonin,
        Double magnesium,
        Double electrolyte
) {
}
