package kr.ac.suwon.dispenser.intake.service;

import java.util.Map;
import java.util.stream.Collectors;
import kr.ac.suwon.dispenser.common.mqtt.MqttService;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.dispenser.service.DispenserService;
import kr.ac.suwon.dispenser.intake.domain.Intake;
import kr.ac.suwon.dispenser.intake.repository.IntakeRepository;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.service.ProfileService;
import kr.ac.suwon.dispenser.rule.RuleContext;
import kr.ac.suwon.dispenser.rule.RuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IntakeService {

    private final IntakeRepository intakeRepository;
    private final ProfileService profileService;
    private final DispenserService dispenserService;
    private final RuleEngine ruleEngine;
    private final MqttService mqttService;

    // 프로필 스냅샷도 내부적으로 추가해야됨
    public Intake recordIntake(Long profileId, String dispenserUuid) {

        Profile profile = profileService.findById(profileId);
        Dispenser dispenser = dispenserService.findByUuid(dispenserUuid);

        log.info("[IntakeService] 요청 수신: profileId=[{}], dispenserUuid=[{}]", profileId, dispenserUuid);

        RuleContext ruleContext = new RuleContext(
                profile.getAge(),
                profile.getHeight(),
                profile.getWeight(),
                profile.getGender(),
                profile.getTags().stream().map(pt -> pt.getTag().getCode()).collect(Collectors.toSet()),
                profile.getConditions().stream().map(pc -> pc.getCondition().getCode()).collect(Collectors.toSet()));

        Map<String, Double> plan = ruleEngine.run(ruleContext);

        if (plan == null || plan.isEmpty()) {
            log.warn("[IntakeService] RuleEngine 결과가 비어있음. plan={}", plan);
        } else {
            log.info("[IntakeService] 배출 계획 산출: {}", plan);
        }
        Double melatonin = plan.get("MELATONIN");
        Double zinc = plan.get("ZINC");
        Double magnesium = plan.get("MAGNESIUM");
        Double electrolyte = plan.get("ELECTROLYTE");
        String profileSnapshot = "\"{}\"";

        log.info("[IntakeService] RuleContext: age={}, height={}, weight={}, gender={}, tags={}, conditions={}",
                ruleContext.age(), ruleContext.height(), ruleContext.weight(), ruleContext.gender(),
                ruleContext.tags(), ruleContext.conditions());


        String commandUuid = profile.getId() + "-" + UUID.randomUUID();
        log.info("[INTAKE] 명령 UUID = {}", commandUuid);
        Intake intake = Intake.create(profile, dispenser, commandUuid, zinc, melatonin, magnesium, electrolyte, profileSnapshot);
        mqttService.publishCommand(dispenserUuid, commandUuid, zinc, melatonin, magnesium, electrolyte);

        intake.markProcessing();
        return intakeRepository.save(intake);
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

    public List<Intake> findAllByProfileId(Long profileId) {
        return intakeRepository.findAllByProfile_Id(profileId);
    }

}
