package KG.Neobis.FMS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTransfer {

    @NotNull
    private BigDecimal sumOfTransaction;

    @NotNull
    private String fromCashAccount;
    private String description;

    @NotNull
    private Date actualDate;

    @NotNull
    private String toCashAccount;
    private String tags;
}
