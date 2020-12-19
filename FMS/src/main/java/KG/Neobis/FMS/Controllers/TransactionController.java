package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ExcelExporter;
import KG.Neobis.FMS.Services.TransactionService;
import KG.Neobis.FMS.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api (value = "Transactions", description = "APIs for transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    @ApiOperation(value = "API for get all of transactions")
    public JournalOfTransaction getAll(RequestFilter conditions){
        return transactionService.getAllTransactionNotDeleted(conditions);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_транзакции')")
    @PostMapping("/income_transaction")
    @ApiOperation(value = "API for post income transaction")
    public ResponseMessage createIncome(@RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createIncomeTransaction(requestTransaction);
        return new ResponseMessage(ResultCode.SUCCESS,"Транзакция успешно добавлена");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_транзакции')")
    @PostMapping("/expense_transaction")
    @ApiOperation(value = "API for post expense transaction")
    public ResponseMessage createExpense( @RequestBody @Valid RequestTransaction requestTransaction){
        transactionService.createExpenseTransaction(requestTransaction);
        return new ResponseMessage(ResultCode.SUCCESS,"Транзакция успешно добавлена");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_транзакции')")
    @PostMapping("/transfer_transaction")
    @ApiOperation(value = "API for post transfer transaction")
    public ResponseMessage createTransfer( @RequestBody @Valid RequestTransfer requestTransfer){
        transactionService.createTransferTransaction(requestTransfer);
        return new ResponseMessage(ResultCode.SUCCESS,"Перевод успешно выполнен");
    }

    @PutMapping("/transactions/{transactionID}")
    public ResponseMessage changeTransactionData(@RequestBody RequestTransaction transaction, Long transactionID){
        transactionService.changeTransaction(transaction,transactionID);
        return new ResponseMessage(ResultCode.SUCCESS, "Данные успешно сохранены");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/delete_transaction/{transactionID}")
    @ApiOperation(value = "API for delete transaction")
    public ResponseMessage delete(@PathVariable Long transactionID){
        transactionService.deleteTransaction(transactionID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удалено!");
    }
}
