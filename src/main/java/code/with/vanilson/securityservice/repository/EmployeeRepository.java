package code.with.vanilson.securityservice.repository;

import code.with.vanilson.securityservice.domain.Employee;
import code.with.vanilson.securityservice.dto.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<EmployeeDTO> findEmployeeByEmail(String email);
}
