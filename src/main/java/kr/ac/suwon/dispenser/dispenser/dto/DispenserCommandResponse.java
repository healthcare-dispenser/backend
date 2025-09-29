package kr.ac.suwon.dispenser.dispenser.dto;

public record DispenserCommandResponse(
        String uuid,
        String commandUuid,
        CommandStatus status
) {
}
