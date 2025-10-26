package kr.ac.suwon.dispenser.dispenser.repository;

import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispenserRepository extends JpaRepository<Dispenser, Long> {
    Optional<Dispenser> findByUuid(String uuid);

    Optional<Dispenser> findByAccount_Id(Long accountId);
    boolean existsByUuid(String uuid);
}
