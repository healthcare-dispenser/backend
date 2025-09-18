package kr.ac.suwon.dispenser.intake.domain;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "intake_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IntakeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dispenser dispenser;

    private Double vitamin;
    private Double melatonin;
    private Double magnesium;
    private Double electrolyte;

    @Column(columnDefinition = "json")
    private String status;

    @Builder(access = AccessLevel.PRIVATE)
    public IntakeLog(Profile profile, Dispenser dispenser, Double vitamin, Double melatonin, Double magnesium, Double electrolyte, String status) {
        this.profile = profile;
        this.dispenser = dispenser;
        this.vitamin = vitamin;
        this.melatonin = melatonin;
        this.magnesium = magnesium;
        this.electrolyte = electrolyte;
        this.status = status;
    }

    public static IntakeLog create(Profile profile, Dispenser dispenser, Double vitamin, Double melatonin, Double magnesium, Double electrolyte, String status) {
        return IntakeLog.builder()
                .profile(profile)
                .dispenser(dispenser)
                .vitamin(vitamin)
                .melatonin(melatonin)
                .magnesium(magnesium)
                .electrolyte(electrolyte)
                .status(status).build();
    }
}
