package code.with.vanilson.securityservice.controller;

import code.with.vanilson.securityservice.domain.Account;
import code.with.vanilson.securityservice.service.impl.AccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/accounts")
public class AccountController {
    private final AccountServiceImpl accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.info("Get all accounts");
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable(name = "username") String username) {
        log.info("Get account by username {}", username);
        return ResponseEntity.ok(accountService.findAccountByUsername(username));
    }

    @PostMapping("/create-account")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid Account account) {
        var accountSaved = accountService.createAccount(account);
        log.info("Account created: {}", accountSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountSaved);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable(name = "id") long id) {
        accountService.deleteAccount(id);
        log.info("DELETE employee by ID: {}", id);
        return ResponseEntity.ok().body(MessageFormat.format("DELETE employee by ID{0}", id));
    }

}
