package kr.ac.suwon.dispenser.profile.domain.condition;

import jakarta.persistence.*;
import lombok.AccessLevel;
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



}
