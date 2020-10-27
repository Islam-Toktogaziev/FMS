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
public class RequestTransaction {

    @NotNull
    private BigDecimal sumOfTransaction;

    private boolean status;

    @NotNull
    private String cashAccount;

    private String description;

    @NotNull
    private Date actualDate;

    @NotNull
    private String category;
    private String contractor;
    private String tags;
}
