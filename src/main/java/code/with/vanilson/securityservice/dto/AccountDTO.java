package code.with.vanilson.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String userName;
    private String password;
    private boolean enabled;
    private boolean locked;
    private boolean expired;
    private boolean credentialExpired;
    private Set<RoleDTO> roles; // Assuming you have a RoleDTO class to represent the Role entity
}
