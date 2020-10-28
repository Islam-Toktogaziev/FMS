package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Services.TransactionService;
import KG.Neobis.FMS.dto.RequestTransaction;
import KG.Neobis.FMS.dto.ResponseTransaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api (value = "Transactions", description = "APIs for transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ModelAttribute("incomeForm")
    public RequestTransaction requestTransaction(){
        return new RequestTransaction();
    }

    @GetMapping("/transactions")
    @ApiOperation(value = "API for get all of transactions")
    public List<ResponseTransaction> getAll(){
        return transactionService.getAllTransactionNotDeleted();
    }

    @PostMapping("/income_transaction")
    @ApiOperation(value = "API for post income transaction")
    public String createIncome( @RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createIncomeTransaction(requestTransaction);
        return "Транзакция успешно добавлена";
    }

    @PostMapping("/expense_transaction")
    @ApiOperation(value = "API for post expense transaction")
    public String createExpense( @RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createExpenseTransaction(requestTransaction);
        return "Транзакция успешно добавлена";
    }

    @PutMapping("/delete_transaction/{transactionID}")
    @ApiOperation(value = "API for delete transaction")
    public void delete(@PathVariable Long transactionID){
        transactionService.deleteTransaction(transactionID);
    }
}
