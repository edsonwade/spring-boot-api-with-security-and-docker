package code.with.vanilson.securityservice.service.impl;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.exception.UserHasNoRolesException;
import code.with.vanilson.securityservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            log.warn(MessageFormat.format("Username {0} not found", username));
            throw new UsernameNotFoundException(MessageFormat.format("User {0} not found", username));
        }
        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            log.warn(MessageFormat.format("User {0} has no roles", username));
            throw new UserHasNoRolesException("User has no roles");
        }
        Collection<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(toList());
        log.info("loadUserByUsername {}", username);
        return new User(account.getUsername(), account.getPassword(), account.isEnabled(),
                !account.isExpired(), !account.isCredentialExpired(), !account.isLocked(), authorities);
    }
}