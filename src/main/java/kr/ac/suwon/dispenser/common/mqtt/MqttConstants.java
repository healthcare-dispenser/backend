package kr.ac.suwon.dispenser.common.mqtt;

public abstract class MqttConstants {
    public static final String DISPENSER_REGISTER_ALL = "dispenser/+/register";
    public static final String DISPENSER_COMMAND_RESPONSE = "dispenser/+/command/response";

    public static String getRegisterResponseTopic(String uuid) {
        return "dispenser/" + uuid + "/register/response";
    }

    public static String getCommandRequestTopic(String uuid) {
        return "dispenser/" + uuid + "/command";
    }
}
