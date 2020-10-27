package KG.Neobis.FMS.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_TAGS", allocationSize = 1)
public class TransactionTags extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "tags")
    @Transient
    @Column(name = "transactions")
    private Set<Transactions> transactions = new HashSet<>();

    public TransactionTags(String name) {
        this.name = name;
    }
}
