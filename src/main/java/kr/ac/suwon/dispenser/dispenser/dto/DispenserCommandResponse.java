package kr.ac.suwon.dispenser.dispenser.dto;

import java.time.LocalDateTime;

public record DispenserCommandResponse(
        String uuid,
        String commandUuid,
        CommandStatus status,
        LocalDateTime completedAt
) {
}
