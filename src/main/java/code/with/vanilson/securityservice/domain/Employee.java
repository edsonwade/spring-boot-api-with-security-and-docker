package code.with.vanilson.securityservice.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue
    private int employeeId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(unique = true)
    private String email;

}
