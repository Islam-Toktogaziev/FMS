package KG.Neobis.FMS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalSumsIncomesAndExpenses {

    private BigDecimal sumOfIncomes;
    private BigDecimal sumOfExpenses;
}
