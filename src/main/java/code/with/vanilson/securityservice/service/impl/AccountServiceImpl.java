package code.with.vanilson.securityservice.service.impl;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.domain.Role;
import code.with.vanilson.securityservice.exception.AccountNotFoundException;
import code.with.vanilson.securityservice.repository.AccountRepository;
import code.with.vanilson.securityservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public List<Account> findAll() {
        log.info("Find all accounts");
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        Role role = roleRepository.findRoleByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setRoles(roles);
        log.info("Create account");
        return accountRepository.save(account);
    }

    public Account findAccountByUsername(String username) {
        var account = accountRepository.findByUsername(username);
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            log.error("Username not found");
            throw new AccountNotFoundException(username);
        }
        log.info("Found account {}", account);
        return account;
    }

    public void deleteAccount(long id) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(MessageFormat.format("Account not found{0}", id)));
        log.info("Delete account");
        accountRepository.delete(account);
    }
}
