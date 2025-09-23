package kr.ac.suwon.dispenser.common.mqtt;

import kr.ac.suwon.dispenser.common.JsonMapper;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserRegisterRequest;
import kr.ac.suwon.dispenser.dispenser.dto.DispenserRegisterResponse;
import kr.ac.suwon.dispenser.dispenser.service.DispenserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static kr.ac.suwon.dispenser.common.mqtt.MqttConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttListener implements MqttCallbackExtended {

    private final MqttClient mqttClient;
    private final MqttService mqttService;
    private final JsonMapper mapper;
    private final DispenserService dispenserService;

    @Value("${mqtt.clean-session}")
    private boolean cleanSession;

    @Value("${mqtt.keep-alive}")
    private int keepAlive;

    @Value("${mqtt.timeout}")
    private int connectionTimeout;

    @EventListener(ApplicationReadyEvent.class)
    public void start() throws Exception {
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setAutomaticReconnect(true);
        opts.setCleanSession(cleanSession);
        opts.setKeepAliveInterval(keepAlive);
        opts.setConnectionTimeout(connectionTimeout);

        mqttClient.setCallback(this);
        mqttClient.connect(opts);

        subscribeAll();

        log.info("[MQTT] 정상적으로 연결 되었습니다. Client-Id = {}", mqttClient.getClientId());
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (Exception e) {
            log.warn("[MQTT] 종료 오류", e);
        }
    }

    void register(String topic, MqttMessage msg) {
        String json = new String(msg.getPayload());
        DispenserRegisterRequest request = mapper.fromJson(json, DispenserRegisterRequest.class);
        String uuid = dispenserService.registerDispenser(request.uuid());

        mqttService.publishRegisterResponse(uuid, DispenserRegisterResponse.ok(uuid));
    }


    private void subscribeAll() throws MqttException {
        mqttClient.subscribe(DISPENSER_REGISTER_ALL, 1, this::register);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        if (reconnect) {
            try {
                log.info("[MQTT] 재연결 - 재구독 시작");
                subscribeAll();
            } catch (Exception e) {
                log.error("[MQTT] 재구독 실패");
            }
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("[MQTT] 연결 끊김", cause);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
