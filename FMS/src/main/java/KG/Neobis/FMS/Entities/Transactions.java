package KG.Neobis.FMS.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_TRANSACTION", allocationSize = 1)
public class Transactions extends BaseEntityAudit {

    private BigDecimal sumOfTransaction;
    private boolean isActual;
    private TypeOfTransaction typeOfTransaction;

    @ManyToOne
    @JoinColumn(name = "from_cash_account",referencedColumnName = "id")
    private CashAccounts fromCashAccount;

    @ManyToOne
    @JoinColumn (name = "to_cash_account", referencedColumnName = "id")
    private CashAccounts toCashAccount;

    private String description;

    @CreatedDate
    private Date actualDate;

    @ManyToOne (optional = true)
    @JoinColumn(name = "incomes_category", referencedColumnName = "id")
    private CategoryForIncomes categoryForIncomes;

    @ManyToOne (optional = true)
    @JoinColumn(name = "expenses_category", referencedColumnName = "id")
    private CategoryForExpenses categoryForExpenses;

    @ManyToOne (optional = true)
    @JoinColumn(name = "contractor",referencedColumnName = "id")
    private Contractors contractor;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "transactions_tags",
            joinColumns = { @JoinColumn(name = "transaction_id") },
            inverseJoinColumns = { @JoinColumn(name = "tags_id") }
    )
    @Column(name = "tags")
    private Set<TransactionTags> tags = new HashSet<>();

    private boolean deleted;

}
