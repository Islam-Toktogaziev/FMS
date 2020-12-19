package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.CashAccountService;
import KG.Neobis.FMS.dto.CashAccountModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import KG.Neobis.FMS.dto.SumInCashAccount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Cash accounts", description = "APIs for cash accounts")
public class CashAccountController {

    private final CashAccountService service;

    public CashAccountController(CashAccountService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/cash_accounts")
    @ApiOperation(value = "API for get all of cash accounts")
    public List<CashAccounts> getAll(){
        return service.getAllAccounts();
    }

    @GetMapping("/cash_accounts/not_archived")
    @ApiOperation(value = "API for get all cash account for dropdown. Returns that are not archived or deleted")
    public List <CashAccountModel> getAllNotArchived(){
        return service.getAllNotArchived();
    }

    @GetMapping("/cash_accounts/not_deleted")
    @ApiOperation("API for get cash accounts for settings, returns that are not deleted")
    public List <CashAccountModel> getAllNotDeleted(){
        return service.getAllNotDeleted();
    }

    @GetMapping("/cash_accounts/{accountID}")
    @ApiOperation(value = "API for get one of cash account by ID")
    public CashAccounts getOneById (@PathVariable Long accountID){
        return service.getOneAccountByID(accountID);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_счета')")
    @PostMapping("/cash_accounts")
    @ApiOperation(value = "API for post cash account")
    public ResponseMessage createAccounts (@RequestBody CashAccountModel cashAccounts){
        service.createAccount(cashAccounts);
        return new ResponseMessage(ResultCode.SUCCESS,"Счет успешно создан");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_счета')")
    @PutMapping("/cash_accounts/{accountID}")
    @ApiOperation(value = "API for change cash in account by ID")
    public ResponseMessage change (@PathVariable Long accountID,
                                       @RequestBody CashAccountModel cashAccounts){
        service.changeAccountDataByID(accountID,cashAccounts);
        return new ResponseMessage(ResultCode.SUCCESS,"Сумма на счету изменено");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/cash_accounts/delete/{cashID}")
    @ApiOperation("Delete cash account by ID")
    public ResponseMessage delecteCashAccount(@PathVariable Long cashID){
        service.deleteCashAccount(cashID);
        return new ResponseMessage(ResultCode.SUCCESS,"Счет успешно удален");
    }

    @PutMapping("/cash_accounts/archive/{accountID}")
    @ApiOperation(value = "API for archive cash account by ID")
    public ResponseMessage change (@PathVariable Long accountID){
        service.archive(accountID);
        return new ResponseMessage(ResultCode.SUCCESS,"данные сохранены");
    }

    @ApiOperation(value = "API for get total sum in Cash accounts")
    @GetMapping("/sum_in_cash_accounts")
    public SumInCashAccount getSumInCashAccounts(){
        return service.getSumInCashAccounts();
    }
}
