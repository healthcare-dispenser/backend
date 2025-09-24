package kr.ac.suwon.dispenser;

import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.repository.AccountRepository;
import kr.ac.suwon.dispenser.dispenser.domain.Dispenser;
import kr.ac.suwon.dispenser.dispenser.repository.DispenserRepository;
import kr.ac.suwon.dispenser.profile.domain.Gender;
import kr.ac.suwon.dispenser.profile.domain.Profile;
import kr.ac.suwon.dispenser.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final AccountRepository accountRepository;
    private final DispenserRepository dispenserRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Account a1 = Account.create("a@a.com", passwordEncoder.encode("123"));
        Account a2 = Account.create("test", passwordEncoder.encode("test"));

        accountRepository.save(a1);
        accountRepository.save(a2);

        Profile p1 = Profile.create(a1, "세현", 180.0, 70.0, Gender.MALE);
        Profile p2 = Profile.create(a1, "다인", 160.0, 40.0, Gender.FEMALE);
        Profile p3 = Profile.create(a1, "다혜", 160.0, 40.0, Gender.FEMALE);
        Profile p4 = Profile.create(a2, "test", 160.0, 40.0, Gender.MALE);

        profileRepository.save(p1);
        profileRepository.save(p2);
        profileRepository.save(p3);
        profileRepository.save(p4);

        Dispenser dispenser = Dispenser.create("TEST-UUID");
        dispenser.assignAccount(a1);

        dispenserRepository.save(dispenser);
    }
}
