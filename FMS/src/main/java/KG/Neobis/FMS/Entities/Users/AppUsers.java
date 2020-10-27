package KG.Neobis.FMS.Entities.Users;

import KG.Neobis.FMS.Entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUsers extends BaseEntity {

    private String name;
    private String email;
    private String number;

    @Transient
    @ManyToMany (mappedBy = "users")
    @Column (name = "roles")
    private Set<Roles> roles;
}
