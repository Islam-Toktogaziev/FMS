package KG.Neobis.FMS.Entities.Users;

import KG.Neobis.FMS.Entities.BaseEntity;
import KG.Neobis.FMS.Entities.BaseEntityAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_ROLES", allocationSize = 1)
public class Roles extends BaseEntityAudit {

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Privilege> privileges = new ArrayList<>();

}
