package kr.ac.suwon.dispenser.profile.service;

import kr.ac.suwon.dispenser.profile.domain.condition.Condition;
import kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode;
import kr.ac.suwon.dispenser.profile.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConditionService {

    private final ConditionRepository conditionRepository;

    public Condition findByCode(ConditionCode conditionCode) {
        return conditionRepository.findByCode(conditionCode).orElseThrow(() -> new RuntimeException("존재하지 않는 조건 입니다."));
    }

    public List<Condition> findAll() {
        return conditionRepository.findAll();
    }
}
