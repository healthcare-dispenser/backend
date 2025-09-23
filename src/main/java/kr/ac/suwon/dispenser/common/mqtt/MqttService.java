package kr.ac.suwon.dispenser.common.mqtt;

import kr.ac.suwon.dispenser.common.JsonMapper;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;
    private final JsonMapper mapper;


    public void publishJson(String topic, Object body) {
        String json = mapper.toJson(body);

    }

    public void publishRegisterResponse(String uuid, DispenserRegisterResponse response) {
        publishJson(MqttConstants.getRegisterResponseTopic(uuid), response);
    }
}
