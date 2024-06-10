package code.with.vanilson.securityservice.mapper;

import code.with.vanilson.securityservice.domain.Employee;
import code.with.vanilson.securityservice.dto.EmployeeDTO;
import code.with.vanilson.securityservice.exception.CannotSaveEmployeeWithNullValuesException;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.stream.Collectors;

/**
 * This class provides methods for mapping {@link Employee} entities to {@link EmployeeDTO} and vice versa,
 * and for validating employee data.
 */
@Component
public class EmployeeMapper {

    /**
     * Maps a list of {@link Employee} entities to a list of {@link EmployeeDTO} objects.
     *
     * @param employees The list of {@link Employee} entities to be mapped.
     * @return The list of mapped {@link EmployeeDTO} objects.
     */
    public List<EmployeeDTO> toEmployeeDTO(List<Employee> employees) {
        return employees.stream()
                .map(this::getEmployeeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts an {@link Employee} entity to an {@link EmployeeDTO} object.
     *
     * @param employee The {@link Employee} entity to be converted.
     * @return The corresponding {@link EmployeeDTO} object.
     * @throws CannotSaveEmployeeWithNullValuesException if any of the employee's fields (firstName, lastName, username, email) is null.
     */
    public EmployeeDTO fromEmployeeDto(Employee employee) {
        // Check for null values
        if (employee.getFirstName() == null ||
                employee.getLastName() == null ||
                employee.getUsername() == null ||
                employee.getEmail() == null) {
            throw new CannotSaveEmployeeWithNullValuesException("Cannot save employee with null values!");
        }
        return getEmployeeDTO(employee);
    }

    /**
     * Maps an {@link Employee} entity to an {@link EmployeeDTO} object.
     *
     * @param employee The {@link Employee} entity to be mapped.
     * @return The corresponding {@link EmployeeDTO} object.
     */
    public EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setUsername(employee.getUsername());
        employeeDTO.setEmail(employee.getEmail());
        return employeeDTO;
    }

    /**
     * Checks if an {@link Employee} entity is valid for saving based on uniqueness constraints.
     *
     * @param employee          The {@link Employee} entity to be validated.
     * @param existingEmployees The list of existing {@link Employee} entities to check against.
     * @return {@code true} if the employee is valid for saving (i.e., username and email are unique), {@code false} otherwise.
     */
    public boolean isEmployeeValidForSave(Employee employee, List<Employee> existingEmployees) {
        // Check if the username and email are unique
        boolean isUsernameUnique = existingEmployees.stream()
                .noneMatch(e -> e.getUsername().equals(employee.getUsername()));
        boolean isEmailUnique = existingEmployees.stream()
                .noneMatch(e -> e.getEmail().equals(employee.getEmail()));

        // Additional checks for the uniqueness of other fields like firstName and lastName can be added similarly
        return isUsernameUnique && isEmailUnique;
    }

    /**
     * Convert DTO to entity
     *
     * @param employeeDTO employeeDTO
     * @return Employee
     */
    public Employee toEmployeeEntity(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .employeeId(employeeDTO.getEmployeeId())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .username(employeeDTO.getUsername())
                .email(employeeDTO.getEmail())
                .build();
    }

}
