package KG.Neobis.FMS.dto;

import KG.Neobis.FMS.Entities.TransactionTags;
import KG.Neobis.FMS.Entities.TypeOfTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransaction {

    private Long id;
    private BigDecimal sumOfTransaction;
    private boolean status;
    private TypeOfTransaction type;
    private String cashAccount;
    private String description;
    private Date actualDate;
    private String category;
    private String contractor;
    private Set<TransactionTags> tags = new HashSet<>();
}
