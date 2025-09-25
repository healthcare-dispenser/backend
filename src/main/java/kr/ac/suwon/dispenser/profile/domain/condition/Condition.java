package kr.ac.suwon.dispenser.profile.domain.condition;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conditions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConditionCode code;
    private String label;

    @Builder(access = AccessLevel.PRIVATE)
    public Condition(ConditionCode code, String label) {
        this.code = code;
        this.label = label;
    }

    public static Condition create(ConditionCode code, String label) {
        return Condition.builder()
                .code(code)
                .label(label).build();
    }
}
