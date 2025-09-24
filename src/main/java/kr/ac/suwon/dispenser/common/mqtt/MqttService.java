package kr.ac.suwon.dispenser.common.mqtt;

import kr.ac.suwon.dispenser.common.JsonMapper;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserCommandRequest;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static kr.ac.suwon.dispenser.common.mqtt.MqttConstants.getCommandRequestTopic;
import static kr.ac.suwon.dispenser.common.mqtt.MqttConstants.getRegisterResponseTopic;

@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;
    private final JsonMapper mapper;


    public void publishJson(String topic, Object body) {
        String json = mapper.toJson(body);
        MqttMessage msg = new MqttMessage(json.getBytes(StandardCharsets.UTF_8));
        msg.setQos(1);
        msg.setRetained(false);

        try {
            mqttClient.publish(topic, msg);
        } catch (MqttException e) {
            throw new RuntimeException("MQTT 퍼블리쉬 오류");
        }
    }

    public void publishCommand(String dispenserUuid, String commandUuid, Double vitamin, Double melatonin, Double magnesium, Double electrolyte) {
        publishJson(getCommandRequestTopic(dispenserUuid), new DispenserCommandRequest(commandUuid, vitamin, melatonin, magnesium, electrolyte));
    }

    public void publishRegisterResponse(String uuid, DispenserRegisterResponse response) {
        publishJson(getRegisterResponseTopic(uuid), response);
    }
}
