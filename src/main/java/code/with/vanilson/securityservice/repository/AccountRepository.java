package code.with.vanilson.securityservice.repository;

import code.with.vanilson.securityservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    boolean existsByUsername(String username);
}
