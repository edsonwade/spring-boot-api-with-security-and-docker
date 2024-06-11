package code.with.vanilson.securityservice.mapper;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.domain.Role;
import code.with.vanilson.securityservice.dto.AccountDTO;
import code.with.vanilson.securityservice.dto.RoleDTO;
import code.with.vanilson.securityservice.exception.AccountCanNotBeNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final PasswordEncoder passwordEncoder;

    public AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            throw new AccountCanNotBeNullException("Account cannot be null");
        }
        return AccountDTO.builder()
                .id(account.getId())
                .userName(account.getUsername())
                .password(passwordEncoder.encode(account.getPassword()))
                .enabled(account.isEnabled())
                .locked(account.isLocked())
                .expired(account.isExpired())
                .credentialExpired(account.isCredentialExpired())
                .roles(account.getRoles()
                        .stream()
                        .map(role -> {
                            new RoleDTO();
                            return RoleDTO
                                    .builder()
                                    .roleId(role.getRoleId())
                                    .name(role.getName())
                                    .code(role.getCode())
                                    .build();

                        }).collect(Collectors.toSet()))
                .build();
    }

    public List<AccountDTO> toAccountDTOList(List<Account> accounts) {
        return accounts.stream()
                .map(this::toAccountDTO)
                .collect(Collectors.toList());
    }

    public Account toAccountEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            throw new AccountCanNotBeNullException("account can not be null");
        }
        return Account.builder()
                .id(accountDTO.getId())
                .username(accountDTO.getUserName())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .enabled(accountDTO.isEnabled())
                .locked(accountDTO.isLocked())
                .expired(accountDTO.isExpired())
                .credentialExpired(accountDTO.isCredentialExpired())
                .roles(accountDTO.getRoles().stream()
                        .map(this::toRoleEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

    RoleDTO toRoleDTO(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDTO.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .code(role.getCode())
                .build();
    }

    public Role toRoleEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        return Role.builder()
                .roleId(roleDTO.getRoleId())
                .name(roleDTO.getName())
                .code(roleDTO.getCode())
                .build();
    }

}
