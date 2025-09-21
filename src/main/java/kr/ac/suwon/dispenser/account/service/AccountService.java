package kr.ac.suwon.dispenser.account.service;

import kr.ac.suwon.dispenser.account.domain.Account;
import kr.ac.suwon.dispenser.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createAccount(String email, String password, String passwordConfirm) {

        if (!password.equals(passwordConfirm)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }

        Account account = Account.create(email, passwordEncoder.encode(password));
        return accountRepository.save(account).getId();
    }

    public Account login(String email, String rawPassword) {
        Account account = findByEmail(email);
        if (!passwordEncoder.matches(rawPassword, account.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return account;
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("계정을 찾을 수 없습니다"));
    }
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));
    }
}
