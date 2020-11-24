package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Services.TotalSumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Api (value = "Total sums", description = "APIs for get total sums")
public class TotalSumController {

    private TotalSumService totalSumService;

    public TotalSumController(TotalSumService totalSumService) {
        this.totalSumService = totalSumService;
    }

    @ApiOperation(value = "API for get total sum of incomes")
    @GetMapping("/incomes")
    public BigDecimal getIncomes(){
        return totalSumService.getSumIncomes();
    }

    @ApiOperation(value = "API for get total sum of expenses")
    @GetMapping("/expenses")
    public BigDecimal getExpenses(){
        return totalSumService.getSumExpenses();
    }

    @ApiOperation(value = "API for get total sum in Cash accounts")
    @GetMapping("/sum_in_cash_accounts")
    public BigDecimal getSumInCashAccounts(){
        return totalSumService.getSumInCashAccounts();
    }
}
