package KG.Neobis.FMS.Entities;

import KG.Neobis.FMS.Enums.TypeOfTransaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @Enumerated
    private TypeOfTransaction typeOfTransaction;

    @ManyToOne
    @JoinColumn(name = "from_cash_account",referencedColumnName = "id")
    private CashAccounts fromCashAccount;

    @NotNull
    @ManyToOne
    @JoinColumn (name = "to_cash_account", referencedColumnName = "id")
    private CashAccounts toCashAccount;

    private String description;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date actualDate;

    @ManyToOne (optional = true)
    @JoinColumn(name = "incomes_category", referencedColumnName = "id")
    private IncomesCategories incomesCategories;

    @ManyToOne (optional = true)
    @JoinColumn(name = "expenses_category", referencedColumnName = "id")
    private ExpensesCategories expensesCategories;

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

    @ManyToOne (optional = true)
    @JoinColumn(name = "projects", referencedColumnName = "id")
    private Projects projects;
}
