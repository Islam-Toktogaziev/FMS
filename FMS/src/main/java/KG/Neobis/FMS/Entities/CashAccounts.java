package KG.Neobis.FMS.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_ACCOUNTS", allocationSize = 1)
public class CashAccounts extends BaseEntity {

    private String name;
    private BigDecimal sumInAccount;

    public CashAccounts(BigDecimal sumInAccount) {
        this.sumInAccount = sumInAccount;
    }
}
