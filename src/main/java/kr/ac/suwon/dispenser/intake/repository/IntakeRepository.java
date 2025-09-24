package kr.ac.suwon.dispenser.intake.repository;

import kr.ac.suwon.dispenser.intake.domain.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntakeRepository extends JpaRepository<Intake, Long> {

    Optional<Intake> findByCommandUuid(String commandUuid);
}
