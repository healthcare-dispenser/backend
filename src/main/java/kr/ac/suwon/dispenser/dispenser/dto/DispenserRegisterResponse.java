package kr.ac.suwon.dispenser.dispenser.dto;

public record DispenserRegisterResponse(
        String uuid,
        String status
) {
    public static DispenserRegisterResponse ok(String uuid) {
        return new DispenserRegisterResponse(uuid, "OK");
    }

    public static DispenserRegisterResponse already(String uuid) {
        return new DispenserRegisterResponse(uuid, "ALREADY");
    }

    public static DispenserRegisterResponse error(String uuid) {
        return new DispenserRegisterResponse(uuid, "ERROR");
    }
}
