package code.with.vanilson.securityservice.mapper;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.domain.Role;
import code.with.vanilson.securityservice.dto.AccountDTO;
import code.with.vanilson.securityservice.dto.RoleDTO;
import code.with.vanilson.securityservice.exception.AccountCanNotBeNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AccountMapperTest {

    @InjectMocks
    private AccountMapper accountMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Convert null AccountDTO to Account entity throws exception")
    void testToAccountDtoNullInputs() {
        // Call the mapper method with null input and verify exception
        assertThrows(AccountCanNotBeNullException.class, () -> accountMapper.toAccountDTO(null));
    }

    @Test
    @DisplayName("Convert Account entity to AccountDTO")
    void testToAccountDTO() {

        // Create test data
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().roleId(1L).name("role1").code("ROLE1").build());
        roles.add(Role.builder().roleId(2L).name("role2").code("ROLE2").build());

        // Create test data
        Account account = new Account();
        account.setId(1L);
        account.setUsername("testuser");
        account.setPassword(passwordEncoder.encode("testpassword"));
        account.setEnabled(true);
        account.setLocked(false);
        account.setExpired(false);
        account.setCredentialExpired(false);
        account.setRoles(roles);

        // Call the mapper method
        AccountDTO accountDTO = accountMapper.toAccountDTO(account);

        // Assertions
        assertEquals(account.getId(), accountDTO.getId());
        assertEquals(account.getUsername(), accountDTO.getUserName());
        assertEquals(account.getPassword(), accountDTO.getPassword());
        assertEquals(account.isEnabled(), accountDTO.isEnabled());
        assertEquals(account.isLocked(), accountDTO.isLocked());
        assertEquals(account.isExpired(), accountDTO.isExpired());
        assertEquals(account.isCredentialExpired(), accountDTO.isCredentialExpired());

        // Convert roles from Role to RoleDTO
        Set<RoleDTO> roleDTOs = roles.stream()
                .map(role -> RoleDTO.builder()
                        .roleId(role.getRoleId())
                        .name(role.getName())
                        .code(role.getCode())
                        .build())
                .collect(Collectors.toSet());

        assertEquals(roleDTOs, accountDTO.getRoles());
    }

    @Test
    @DisplayName("Convert List<Account> to List<AccountDTO>")
    void testToAccountDTOList() {
        // Create test data
        Account account = new Account();
        account.setId(1L);
        account.setUsername("testuser");
        account.setPassword(passwordEncoder.encode("testpassword"));
        account.setEnabled(true);
        account.setLocked(false);
        account.setExpired(false);
        account.setCredentialExpired(false);
        account.setRoles(Collections.emptySet());
        List<Account> accountList = Collections.singletonList(account);

        // Call the mapper method
        List<AccountDTO> accountDTOList = accountMapper.toAccountDTOList(accountList);

        // Assertions
        assertEquals(1, accountDTOList.size());
        assertEquals(account.getId(), accountDTOList.get(0).getId());
        assertEquals(account.getUsername(), accountDTOList.get(0).getUserName());
        assertEquals(account.getPassword(), accountDTOList.get(0).getPassword());
        assertEquals(account.isEnabled(), accountDTOList.get(0).isEnabled());
        assertEquals(account.isLocked(), accountDTOList.get(0).isLocked());
        assertEquals(account.isExpired(), accountDTOList.get(0).isExpired());
        assertEquals(account.isCredentialExpired(), accountDTOList.get(0).isCredentialExpired());
        assertEquals(account.getRoles(), accountDTOList.get(0).getRoles());
    }

    // Add tests for other mapper methods (toAccountEntity, toRoleDTO, toRoleEntity)

    @Test
    @DisplayName("Convert Role entity to RoleDTO")
    void testToRoleDTO() {
        // Create test data
        Role role = new Role();
        role.setRoleId(1L);
        role.setName("testrole");
        role.setCode("TEST_ROLE");

        // Call the mapper method
        RoleDTO roleDTO = accountMapper.toRoleDTO(role);

        // Assertions
        assertEquals(role.getRoleId(), roleDTO.getRoleId());
        assertEquals(role.getName(), roleDTO.getName());
        assertEquals(role.getCode(), roleDTO.getCode());
    }

    @Test
    @DisplayName("Convert null Role entity to null RoleDTO")
    void testToRoleDTOMissingRole() {
        // Call the mapper method with null input
        RoleDTO roleDTO = accountMapper.toRoleDTO(null);

        // Assertion
        assertNull(roleDTO);
    }

    @Test
    @DisplayName("Convert RoleDTO to Role entity")
    void testToRoleEntity() {
        // Create test data
        RoleDTO roleDTO = RoleDTO.builder()
                .roleId(1L)
                .name("testrole")
                .code("TEST_ROLE")
                .build();

        // Call the mapper method
        Role role = accountMapper.toRoleEntity(roleDTO);

        // Assertions
        assertEquals(roleDTO.getRoleId(), role.getRoleId());
        assertEquals(roleDTO.getName(), role.getName());
        assertEquals(roleDTO.getCode(), role.getCode());
    }

    @Test
    @DisplayName("Convert null RoleDTO to null Role entity")
    void testToRoleEntityMissingRoleDTO() {
        // Call the mapper method with null input
        Role role = accountMapper.toRoleEntity(null);

        // Assertion
        assertNull(role);
    }

    @Test
    @DisplayName("Convert AccountDTO to Account entity")
    void testToAccountEntity() {
        // Create test data
        AccountDTO accountDTO = AccountDTO.builder()
                .id(1L)
                .userName("testuser")
                .password(passwordEncoder.encode("testpassword"))
                .enabled(true)
                .locked(false)
                .expired(false)
                .credentialExpired(false)
                .roles(Collections.singleton(RoleDTO.builder()
                        .roleId(1L)
                        .name("testrole")
                        .code("TEST_ROLE")
                        .build()))
                .build();

        // Call the mapper method
        Account account = accountMapper.toAccountEntity(accountDTO);

        // Assertions
        assertEquals(accountDTO.getId(), account.getId());
        assertEquals(accountDTO.getUserName(), account.getUsername());
        assertEquals(accountDTO.getPassword(), account.getPassword());
        assertEquals(accountDTO.isEnabled(), account.isEnabled());
        assertEquals(accountDTO.isLocked(), account.isLocked());
        assertEquals(accountDTO.isExpired(), account.isExpired());
        assertEquals(accountDTO.isCredentialExpired(), account.isCredentialExpired());
        assertEquals(accountDTO.getRoles().size(), account.getRoles().size());
    }

    @Test
    @DisplayName("Convert null AccountDTO to Account entity throws exception")
    void testToAccountEntityNullInput() {
        // Call the mapper method with null input and verify exception
        assertThrows(AccountCanNotBeNullException.class, () -> accountMapper.toAccountEntity(null));
    }

}
