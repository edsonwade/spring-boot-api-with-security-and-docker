package code.with.vanilson.securityservice.service;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.dto.AccountDTO;
import code.with.vanilson.securityservice.exception.AccountNotFoundException;
import code.with.vanilson.securityservice.exception.UsernameAlreadyExistsException;
import code.with.vanilson.securityservice.mapper.AccountMapper;
import code.with.vanilson.securityservice.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Find All Accounts - Success")
    void testFindAllAccounts_Success() {
        // Mock repository response
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        // Mock mapper response
        List<AccountDTO> accountDTOs = new ArrayList<>();
        when(accountMapper.toAccountDTOList(accounts)).thenReturn(accountDTOs);

        // Call the service method
        List<AccountDTO> result = accountService.findAllAccounts();

        // Assertions
        Assertions.assertEquals(accountDTOs, result);

        // Verify interactions
        verify(accountRepository).findAll();
        verify(accountMapper).toAccountDTOList(accounts);

    }

    @Test
    @DisplayName("Find Account By Id - Success")
    void testFindAccountById_Success() {
        // Mock repository response
        Account account = new Account();
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Mock mapper response
        AccountDTO accountDTO = new AccountDTO();
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        // Call the service method
        Optional<AccountDTO> result = accountService.findAccountById(accountId);

        // Assertions
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accountDTO, result.get());

        // Verify interactions
        verify(accountRepository).findById(accountId);
        verify(accountMapper).toAccountDTO(account);


    }

    @Test
    @DisplayName("Create Account - Success")
    void testCreateAccount_Success() {
        // Prepare test data
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserName("testuser");
        accountDTO.setPassword("testpassword");

        // Mock repository response (username does not exist)
        when(accountRepository.existsByUsername(accountDTO.getUserName())).thenReturn(false);

        // Mock mapper response
        Account account = new Account();
        when(accountMapper.toAccountEntity(accountDTO)).thenReturn(account);
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        // Mock repository save
        when(accountRepository.save(account)).thenReturn(account);

        // Call the service method
        AccountDTO result = accountService.createAccount(accountDTO);

        // Verify interactions
        verify(accountRepository).existsByUsername(accountDTO.getUserName());
        verify(accountMapper).toAccountEntity(accountDTO);
        verify(accountRepository).save(account);
        verify(accountMapper).toAccountDTO(account);

        // Assertions
        Assertions.assertEquals(accountDTO, result);
    }

    @Test
    @DisplayName("Create Account - Username Already Exists")
    void testCreateAccount_UsernameExists() {
        // Prepare test data
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserName("existinguser");
        accountDTO.setPassword("testpassword");

        // Mock repository response (username already exists)
        when(accountRepository.existsByUsername(accountDTO.getUserName())).thenReturn(true);

        // Call the service method and verify that it throws the expected exception
        Assertions.assertThrows(
                UsernameAlreadyExistsException.class,
                () -> accountService.createAccount(accountDTO)
        );

        // Verify interactions
        verify(accountRepository).existsByUsername(accountDTO.getUserName());
        // Ensure that other interactions (save, mapper calls) do not happen
    }




    @Test
    @DisplayName("Update Account - Success")
    void testUpdateAccount_Success() {
        // Prepare test data
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountId);
        accountDTO.setUserName("newusername");
        accountDTO.setPassword("newpassword");
        accountDTO.setRoles(Collections.emptySet());

        // Mock repository response (existing account found)
        Account account = new Account();
        account.setId(1L);
        account.setUsername("testuser");
        account.setPassword("testpassword");
        account.setEnabled(true);
        account.setLocked(false);
        account.setExpired(false);
        account.setCredentialExpired(false);
        account.setRoles(Collections.emptySet());
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsByUsername(accountDTO.getUserName())).thenReturn(false);

        // Mock mapper response
        AccountDTO mappedAccountDTO = accountMapper.toAccountDTO(account);
        when(accountMapper.toAccountDTO(account)).thenReturn(mappedAccountDTO);

        // Call the service method
        AccountDTO result = accountService.updateAccount(accountId, accountDTO);

        // Verify interactions
        verify(accountRepository).findById(accountId);
        verify(accountRepository).existsByUsername(accountDTO.getUserName());
        verify(accountRepository).save(account);
       verify(accountMapper).toAccountDTO(account);

        // Assertions
        Assertions.assertEquals(mappedAccountDTO, result);
    }



    @Test
    @DisplayName("Update Account - Username Already Exists")
    void testUpdateAccount_UsernameExists() {
        // Prepare test data
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountId);
        accountDTO.setUserName("existinguser");
        accountDTO.setPassword("newpassword");
        accountDTO.setRoles(Collections.emptySet());

        // Mock repository response (existing account found)
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setUsername("existinguser");
        existingAccount.setRoles(Collections.emptySet());
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.existsByUsername(accountDTO.getUserName())).thenReturn(true);

        // Call the service method and verify that it throws the expected exception
        Assertions.assertThrows(
                UsernameAlreadyExistsException.class,
                () -> accountService.updateAccount(accountId, accountDTO)
        );

        // Verify interactions
        verify(accountRepository).findById(accountId);
        verify(accountRepository).existsByUsername(accountDTO.getUserName());
        // Ensure that other interactions (save, mapper calls) do not happen
    }



    @Test
    @DisplayName("Delete Account - Success")
    void testDeleteAccount_Success() {
        // Prepare test data
        Long accountId = 1L;

        // Mock repository response (existing account found)
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        // Call the service method
        accountService.deleteAccount(accountId);

        // Verify interactions
        verify(accountRepository).findById(accountId);
        verify(accountRepository).delete(existingAccount);
    }

    @Test
    @DisplayName("Delete Account - Not Found")
    void testDeleteAccount_NotFound() {
        // Prepare test data
        Long accountId = 1L;

        // Mock repository response (no account found)
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Call the service method and verify that it throws the expected exception
        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> accountService.deleteAccount(accountId)
        );

        // Verify interactions
        verify(accountRepository).findById(accountId);
        // Ensure that delete method is not called
    }


}