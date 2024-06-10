package code.with.vanilson.securityservice.service;

import code.with.vanilson.securityservice.domain.Employee;
import code.with.vanilson.securityservice.dto.EmployeeDTO;
import code.with.vanilson.securityservice.exception.CannotSaveEmployeeWithNullValuesException;
import code.with.vanilson.securityservice.exception.EmployeeNotFoundException;
import code.with.vanilson.securityservice.exception.EmployeeWithEmailAlreadyExistException;
import code.with.vanilson.securityservice.mapper.EmployeeMapper;
import code.with.vanilson.securityservice.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /**
     * Constructs an EmployeeService with the specified EmployeeRepository and EmployeeMapper.
     *
     * @param employeeRepository the repository for employee data
     * @param employeeMapper     the mapper for converting between Employee and EmployeeDTO
     */
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of EmployeeDTO representing all employees
     */
    public List<EmployeeDTO> getAllEmployees() {
        var employees = employeeRepository.findAll();
        log.info("Employees found: {}", employees);
        return employeeMapper.toEmployeeDTO(employees);
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id the ID of the employee to retrieve
     * @return an Optional containing the EmployeeDTO if found, or empty if not found
     * @throws EmployeeNotFoundException if the employee is not found
     */
    public Optional<EmployeeDTO> getEmployeeById(int id) {
        return Optional.ofNullable(employeeRepository
                .findById(id)
                .stream()
                .map(employeeMapper::getEmployeeDTO)
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new));
    }

    /**
     * Retrieves an employee by their email.
     *
     * @param email the email of the employee to retrieve
     * @return an Optional containing the EmployeeDTO if found, or empty if not found
     * @throws EmployeeNotFoundException if the employee is not found
     */
    public Optional<EmployeeDTO> getEmployeeByEmail(String email) {
        return Optional.ofNullable(employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(EmployeeNotFoundException::new));
    }

    /**
     * Creates a new employee.
     *
     * @param employeeDTO the EmployeeDTO representing the employee to create
     * @return the created EmployeeDTO
     * @throws CannotSaveEmployeeWithNullValuesException if any required field is null
     * @throws IllegalArgumentException                  if the username or email is not unique
     */
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Convert DTO to entity
        Employee employee = employeeMapper.toEmployeeEntity(employeeDTO);

        // Check for null values
        if (employee.getFirstName() == null ||
                employee.getLastName() == null ||
                employee.getUsername() == null ||
                employee.getEmail() == null) {
            log.error("Employee creation failed due to null values");
            throw new CannotSaveEmployeeWithNullValuesException("Cannot save employee with null values!");
        }

        List<Employee> existingEmployees = employeeRepository.findAll();
        if (employeeMapper.isEmployeeValidForSave(employee, existingEmployees)) {
            Employee savedEmployee = employeeRepository.save(employee);
            log.info("Employee saved: {}", savedEmployee);
            return employeeMapper.getEmployeeDTO(savedEmployee);
        }

        // Check uniqueness before saving
        throw new EmployeeWithEmailAlreadyExistException();
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     * @throws EmployeeNotFoundException if the employee is not found
     */
    public void deleteEmployee(Integer id) {
        var deleteSuccess = new AtomicReference<>(employeeRepository
                .findById(id)
                .orElseThrow(EmployeeNotFoundException::new));
        employeeRepository.deleteById(deleteSuccess.get().getEmployeeId());
        log.info("Employee deleted: {}", deleteSuccess.get());
    }
}
