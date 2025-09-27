package kr.ac.suwon.dispenser.dispenser.domain;

import jakarta.persistence.*;
import kr.ac.suwon.dispenser.account.domain.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dispensers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Dispenser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispenser_id")
    private Long id;

    private String uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private DispenserStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    public Dispenser(String uuid) {
        this.uuid = uuid;
    }

    public static Dispenser create(String uuid) {
        Dispenser dispenser = Dispenser.builder()
                .uuid(uuid).build();

        dispenser.status = DispenserStatus.REGISTERED;
        return dispenser;
    }

    public void assignAccount(Account account) {
        this.status = DispenserStatus.ACTIVE;
        this.account = account;
    }

    public void updateStatus(DispenserStatus status) {
        this.status = status;
    }
}
