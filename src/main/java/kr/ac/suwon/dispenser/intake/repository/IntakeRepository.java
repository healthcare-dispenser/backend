package kr.ac.suwon.dispenser.intake.repository;

import java.time.LocalDateTime;
import kr.ac.suwon.dispenser.intake.domain.Intake;
import kr.ac.suwon.dispenser.intake.dto.csv.Csv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IntakeRepository extends JpaRepository<Intake, Long> {

    Optional<Intake> findByCommandUuid(String commandUuid);

    List<Intake> findAllByProfile_Id(Long profileId);

    @Query("""
        SELECT new kr.ac.suwon.dispenser.intake.dto.csv.Csv(
            i.id,
            i.profile.id,
            i.commandUuid,
            i.status,
            i.requestedAt,
            i.completedAt,
            i.zinc,
            i.melatonin,
            i.magnesium,
            i.electrolyte,
            f.id,
            f.sleepRating,
            f.fatigueRating,
            f.createdAt
        )
        FROM Intake i
        LEFT JOIN Feedback f
          ON f.profile = i.profile
         AND f.createdAt = (
             SELECT MIN(ff.createdAt)
             FROM Feedback ff
             WHERE ff.profile = i.profile
               AND function('date', ff.createdAt) = function('date', COALESCE(i.completedAt, i.requestedAt))
               AND ff.createdAt >= COALESCE(i.completedAt, i.requestedAt)
         )
        WHERE (:profileId IS NULL OR i.profile.id = :profileId)
          AND (:from IS NULL OR i.completedAt >= :from)
          AND (:to   IS NULL OR i.completedAt <  :to)
        ORDER BY i.completedAt DESC, i.id DESC
    """)
    List<Csv> exportCsv(
            @Param("profileId") Long profileId,
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to
    );
}
