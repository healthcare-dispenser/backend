package kr.ac.suwon.dispenser.dispenser.domain;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.account.domain.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dispensers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Dispenser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private DispenserStatus status;


}
