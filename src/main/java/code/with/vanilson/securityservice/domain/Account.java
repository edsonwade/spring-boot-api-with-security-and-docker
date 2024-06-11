package code.with.vanilson.securityservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @Column(name = "account_id")
    private Long id;


    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", length = 200)
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    private boolean enabled = true;
    private boolean locked = false;
    private boolean expired = false;

    @Column(name = "credential_expired")
    private boolean credentialExpired = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "AccountRole",
            joinColumns = @JoinColumn(name = "accountId", referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "role_id")
    )
    private Set<Role> roles;
}

