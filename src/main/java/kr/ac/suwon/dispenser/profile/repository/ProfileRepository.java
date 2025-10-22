package kr.ac.suwon.dispenser.profile.repository;

import kr.ac.suwon.dispenser.profile.domain.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByAccount_Id(Long accountId);

    int deleteByIdAndAccount_Id(Long profileId, Long accountId);

    @EntityGraph(attributePaths = {
            "tags", "tags.tag",
            "conditions", "conditions.condition"
    })
    Optional<Profile> findByIdAndAccount_Id(Long profileId, Long accountId);
}
