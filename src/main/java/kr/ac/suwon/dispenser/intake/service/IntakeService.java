package kr.ac.suwon.dispenser.intake.service;

import kr.ac.suwon.dispenser.common.mqtt.MqttService;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.dispenser.service.DispenserService;
import kr.ac.suwon.dispenser.intake.domain.Intake;
import kr.ac.suwon.dispenser.intake.repository.IntakeRepository;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class IntakeService {

    private final IntakeRepository intakeRepository;
    private final ProfileService profileService;
    private final DispenserService dispenserService;
    private final MqttService mqttService;

    // RuleEngine 기반으로 교체 예정 지금은 값 고정해서 보내기
    // 프로필 스냅샷도 내부적으로 추가해야됨
    public Long recordIntake(Long profileId, String dispenserUuid) {

        Profile profile = profileService.findById(profileId);
        Dispenser dispenser = dispenserService.findByUuid(dispenserUuid);
        Double vitamin = 1.0;
        Double melatonin = 1.0;
        Double magnesium = 1.0;
        Double electrolyte = 1.0;
        String profileSnapshot = "default";


        String commandUuid = profile.getId() + "-" + UUID.randomUUID();
        Intake intake = Intake.create(profile, dispenser, commandUuid, vitamin, melatonin, magnesium, electrolyte, profileSnapshot);
        mqttService.publishCommand(dispenserUuid, dispenserUuid, vitamin, melatonin, magnesium, electrolyte);

        intake.markProcessing();
        return intakeRepository.save(intake).getId();
    }

    public void recordSuccess(String commandUuid) {
        Intake intake = findByCommandUuid(commandUuid);
        intake.markSuccess();
    }

    public void recordFail(String commandUuid) {
        Intake intake = findByCommandUuid(commandUuid);
        intake.markFail();
    }

    public Intake findById(Long intakeId) {
        return intakeRepository.findById(intakeId).orElseThrow(() -> new RuntimeException("존재하지 않는 섭취기록 입니다."));
    }

    public Intake findByCommandUuid(String commandUuid) {
        return intakeRepository.findByCommandUuid(commandUuid).orElseThrow(() -> new RuntimeException("존재하지 않는 섭취기록 입니다."));
    }

}
