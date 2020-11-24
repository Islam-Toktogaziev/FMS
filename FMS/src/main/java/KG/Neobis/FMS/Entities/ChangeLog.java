package KG.Neobis.FMS.Entities;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_CHANGE_LOG", allocationSize = 1)
public class ChangeLog extends BaseEntity {

    @OneToOne
    private AppUsers user;
}
