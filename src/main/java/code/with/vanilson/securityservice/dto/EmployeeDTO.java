package code.with.vanilson.securityservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDTO {
    private int employeeId;
    @NotNull(message = "first name is required")
    private String firstName;
    @NotNull(message = "last name is required")
    private String lastName;
    @NotNull(message = "user name is required")
    private String username;
    @NotNull(message = "email name is required")
    @NotEmpty(message = "email name is cannot be empty")
    private String email;
}
