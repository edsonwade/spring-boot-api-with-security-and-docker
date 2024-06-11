package code.with.vanilson.securityservice.service;

import code.with.vanilson.securityservice.dto.AccountDTO;
import code.with.vanilson.securityservice.exception.AccountNotFoundException;
import code.with.vanilson.securityservice.exception.UsernameAlreadyExistsException;
import code.with.vanilson.securityservice.mapper.AccountMapper;
import code.with.vanilson.securityservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a list of all accounts.
     *
     * @return List of AccountDTO containing all accounts.
     */

    public List<AccountDTO> findAllAccounts() {
        var accounts = accountRepository.findAll();
        log.info("Found {} accounts", accounts.size());
        return accountMapper.toAccountDTOList(accounts);

    }

    /**
     * Finds an account by its ID.
     *
     * @param id The ID of the account to find.
     * @return Optional containing the AccountDTO if found, empty otherwise.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    public Optional<AccountDTO> findAccountById(Long id) {
        var accounts = accountRepository.
                findById(id)
                .orElseThrow(AccountNotFoundException::new);
        log.info("Found account with id {}", id);
        return Optional.ofNullable(accountMapper.toAccountDTO(accounts));

    }

    /**
     * Creates a new account.
     *
     * @param accountDTO The AccountDTO containing the details of the account to create.
     * @return The created AccountDTO.
     * @throws UsernameAlreadyExistsException if an account with the same username already exists.
     */
    public AccountDTO createAccount(AccountDTO accountDTO) {
        // Check if username already exists
        if (accountRepository.existsByUsername(accountDTO.getUserName())) {
            usernameAlreadyExists();
            throw new UsernameAlreadyExistsException(
                    MessageFormat.format("Username already exists: {0}", accountDTO.getUserName()));
        }
        var account = accountMapper.toAccountEntity(accountDTO);
        var accountSaved = accountRepository.save(account);
        log.info("Account saved: {}", accountSaved);
        return accountMapper.toAccountDTO(accountSaved);

    }

    /**
     * Updates an existing account.
     *
     * @param id         The ID of the account to update.
     * @param accountDTO The AccountDTO containing the updated details.
     * @return The updated AccountDTO.
     * @throws AccountNotFoundException       if no account is found with the given ID.
     * @throws UsernameAlreadyExistsException if an account with the updated username already exists.
     */
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        var existingAccount = accountRepository.findById(id)
                .orElseThrow(AccountNotFoundException::new);

        // Check if username already exists (excluding the current account)
        if (accountRepository.existsByUsername(accountDTO.getUserName())) {
            usernameAlreadyExists();
            throw new UsernameAlreadyExistsException(
                    MessageFormat.format("Username already exists: {0}", accountDTO.getUserName()));
        }

        // Update the existing account entity with the new details
        // Validate if the new username is the same as the existing one
        if (!existingAccount.getUsername().equals(accountDTO.getUserName())) {
            // Update the existing account entity with the new username
            existingAccount.setUsername(accountDTO.getUserName());
        }
        existingAccount.setPassword(accountDTO.getPassword());
        existingAccount.setEnabled(accountDTO.isEnabled());
        existingAccount.setLocked(accountDTO.isLocked());
        existingAccount.setExpired(accountDTO.isExpired());
        existingAccount.setCredentialExpired(accountDTO.isCredentialExpired());
        existingAccount.setRoles(accountDTO.getRoles().stream()
                .map(accountMapper::toRoleEntity)
                .collect(Collectors.toSet()));

        var updatedAccount = accountRepository.save(existingAccount);
        log.info("Account updated: {}", updatedAccount);
        return accountMapper.toAccountDTO(updatedAccount);
    }

    /**
     * Deletes an account by its ID.
     *
     * @param id The ID of the account to delete.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    public void deleteAccount(Long id) {
        var account = accountRepository.findById(id)
                .orElseThrow(AccountNotFoundException::new);
        log.info("Deleting account with id:{}", id);
        accountRepository.delete(account);
    }

    /**
     * return a message with username already exists
     */
    private static void usernameAlreadyExists() {
        log.error("Username already exists");
    }
}
