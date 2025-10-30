package kr.ac.suwon.dispenser.dispenser.service;

import java.util.Optional;
import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.service.AccountService;
import kr.ac.suwon.dispenser.common.mqtt.MqttService;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.dispenser.repository.DispenserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DispenserService {

    private final DispenserRepository dispenserRepository;
    private final MqttService mqttService;
    private final AccountService accountService;

    public String createDispenser(String uuid) {
        Dispenser dispenser = Dispenser.create(uuid);
        return dispenserRepository.save(dispenser).getUuid();
    }

    public String registerDispenser(String uuid) {
        if (isExistByUuid(uuid)) {
            return uuid;
        } else {
            return createDispenser(uuid);
        }
    }

    public void assignAccount(Long accountId, String uuid) {
        Account account = accountService.findById(accountId);
        Dispenser dispenser = findByUuid(uuid);

        findByAccountId(accountId).ifPresent(Dispenser::unAssignAccount);
        dispenserRepository.flush();
        dispenser.unAssignAccount();
        dispenser.assignAccount(account);
    }

    public Dispenser findByUuid(String uuid) {
        return dispenserRepository.findByUuid(uuid)
                .orElseThrow(() -> {
                    log.error("[DispenserService] 존재하지 않는 디스펜서 요청 - uuid={}", uuid);
                    return new RuntimeException("존재하지 않는 디스펜서 입니다.");
                });
    }

    public Optional<Dispenser> findByAccountId(Long accountId) {
        return dispenserRepository.findByAccount_Id(accountId);
    }

    public void requestWash(String dispenserUuid, int slot) {
        mqttService.publishWash(dispenserUuid, slot);
    }

    public boolean isExistByUuid(String uuid) {
        return dispenserRepository.existsByUuid(uuid);
    }
}
