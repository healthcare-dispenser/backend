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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final AccountRepository accountRepository;
    private final DispenserRepository dispenserRepository;
    private final ProfileRepository profileRepository;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Account account = Account.create("a@a.com", "123");
        accountRepository.save(account);

        Profile p1 = Profile.create(account, "세현", 180.0, 70.0, Gender.MALE);
        Profile p2 = Profile.create(account, "다인", 160.0, 40.0, Gender.FEMALE);
        Profile p3 = Profile.create(account, "다혜", 160.0, 40.0, Gender.FEMALE);

        profileRepository.save(p1);
        profileRepository.save(p2);
        profileRepository.save(p3);

        Dispenser dispenser = Dispenser.create("TEST_UUID");
        dispenser.assignAccount(account);
        dispenserRepository.save(dispenser);


    }
}
