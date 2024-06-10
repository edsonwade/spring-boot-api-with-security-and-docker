package code.with.vanilson.securityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import code.with.vanilson.securityservice.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByName(String roleUser);
}
