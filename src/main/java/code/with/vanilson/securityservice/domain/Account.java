package code.with.vanilson.securityservice.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;
    @Column(name = "username",unique = true)
    private String userName;
    @Column(name = "password", length = 10)
    private String password;
    private boolean enabled = true;
    private boolean locked = false;
    private boolean expired = false;
    @Column(name = "credential_expired")
    private boolean credentialExpired = false;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "AccountRole",
            joinColumns = @JoinColumn(name = "accountId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId"))
    private Set<Role> roles;

}
