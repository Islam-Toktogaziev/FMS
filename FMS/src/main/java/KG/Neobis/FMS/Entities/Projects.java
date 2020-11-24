package KG.Neobis.FMS.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_PROJECTS", allocationSize = 1)
public class Projects extends BaseEntityAudit {

    private String name;

    @Transient
    @OneToMany
    @JoinColumn(name = "transactions", referencedColumnName = "id")
    private Set<Transactions> transactions;
}
