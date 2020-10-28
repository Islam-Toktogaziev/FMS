package KG.Neobis.FMS.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_CONTRACTORS", allocationSize = 1)
public class Contractors extends BaseEntity {

    private String name;
}
