package code.with.vanilson.securityservice.service.impl;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.domain.Role;
import code.with.vanilson.securityservice.repository.AccountRepository;
import code.with.vanilson.securityservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        Role role = roleRepository.findRoleByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setRoles(roles);
        return accountRepository.save(account);
    }

    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }
}
