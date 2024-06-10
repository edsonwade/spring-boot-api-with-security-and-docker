package code.with.vanilson.securityservice.controller;

import code.with.vanilson.securityservice.dto.EmployeeDTO;
import code.with.vanilson.securityservice.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of EmployeeDTO representing all employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        log.info("GET all employees");
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id the ID of the employee to retrieve
     * @return the EmployeeDTO representing the employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<EmployeeDTO>> getEmployeeById(@PathVariable int id) {
        log.info("GET employee by ID: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * Retrieves an employee by their email.
     *
     * @param email the email of the employee to retrieve
     * @return the EmployeeDTO representing the employee
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<EmployeeDTO>> getEmployeeByEmail(@PathVariable String email) {

        log.info("GET employee by email: {}", email);
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    /**
     * Creates a new employee.
     *
     * @param employeeDTO the EmployeeDTO representing the employee to create
     * @return the created EmployeeDTO
     * @throws IllegalArgumentException if the username or email is not unique
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        log.info("POST create employee");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        log.info("DELETE employee by ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
