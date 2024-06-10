package code.with.vanilson.securityservice.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long roleId;
    private String name;
    private String code;

}
