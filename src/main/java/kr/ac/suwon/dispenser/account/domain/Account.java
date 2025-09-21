package kr.ac.suwon.dispenser.account.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String email;
    private String password;


    @Builder(access = AccessLevel.PRIVATE)
    public Account( String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Account create(String email, String password) {
        return Account.builder()
                .email(email)
                .password(password).build();
    }
}
