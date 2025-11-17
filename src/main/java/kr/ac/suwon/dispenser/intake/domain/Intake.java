package kr.ac.suwon.dispenser.intake.domain;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "intakes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Intake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispenser_id")
    private Dispenser dispenser;

    private String commandUuid;
    private Double zinc;
    private Double melatonin;
    private Double magnesium;
    private Double electrolyte;

    @Enumerated(EnumType.STRING)
    private IntakeStatus status;

    @Column(columnDefinition = "json")
    private String profileSnapshot;

    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;



    @Builder(access = AccessLevel.PRIVATE)
    public Intake(Profile profile, Dispenser dispenser, String commandUuid, Double zinc, Double melatonin, Double magnesium, Double electrolyte,
                  IntakeStatus status, String profileSnapshot) {
        this.profile = profile;
        this.dispenser = dispenser;
        this.commandUuid = commandUuid;
        this.zinc = zinc;
        this.melatonin = melatonin;
        this.magnesium = magnesium;
        this.electrolyte = electrolyte;
        this.status = status;
        this.profileSnapshot = profileSnapshot;
    }

    public static Intake create(Profile profile, Dispenser dispenser,String commandUuid, Double zinc,
                                Double melatonin, Double magnesium, Double electrolyte, String profileSnapshot) {
        Intake intake = Intake.builder()
                .profile(profile)
                .dispenser(dispenser)
                .commandUuid(commandUuid)
                .zinc(zinc)
                .melatonin(melatonin)
                .magnesium(magnesium)
                .electrolyte(electrolyte)
                .profileSnapshot(profileSnapshot).build();

        intake.status = IntakeStatus.REQUESTED;
        intake.requestedAt = LocalDateTime.now();
        return intake;
    }

    public void markProcessing() {
        this.status = IntakeStatus.PROCESSING;
    }

    public void markSuccess() {
        this.status = IntakeStatus.SUCCESS;
        this.completedAt = LocalDateTime.now();
    }

    public void markFail() {
        this.status = IntakeStatus.FAIL;
    }
}
