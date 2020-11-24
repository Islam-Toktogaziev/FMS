package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.TransactionService;
import KG.Neobis.FMS.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@Api (value = "Transactions", description = "APIs for transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    @ApiOperation(value = "API for get all of transactions")
    public List<ResponseTransaction> getAll(RequestFilter conditions) throws ParseException {
        return transactionService.getAllTransactionNotDeleted(conditions);
    }

    @PostMapping("/income_transaction")
    @ApiOperation(value = "API for post income transaction")
    public ResponseMessage createIncome(@RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createIncomeTransaction(requestTransaction);
        return new ResponseMessage(ResultCode.SUCCESS,"Транзакция успешно добавлена");
    }

    @PostMapping("/expense_transaction")
    @ApiOperation(value = "API for post expense transaction")
    public ResponseMessage createExpense( @RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createExpenseTransaction(requestTransaction);
        return new ResponseMessage(ResultCode.SUCCESS,"Транзакция успешно добавлена");
    }

    @PostMapping("/transfer_transaction")
    @ApiOperation(value = "API for post transfer transaction")
    public ResponseMessage createTransfer( @RequestBody @Valid RequestTransfer requestTransfer){
        transactionService.createTransferTransaction(requestTransfer);
        return new ResponseMessage(ResultCode.SUCCESS,"Перевод успешно выполнен");
    }

    @DeleteMapping("/delete_transaction/{transactionID}")
    @ApiOperation(value = "API for delete transaction")
    public ResponseMessage delete(@PathVariable Long transactionID){
        transactionService.deleteTransaction(transactionID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удалено!");
    }
}
