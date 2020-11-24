package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.CashAccountService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Cash accounts", description = "APIs for cash accounts")
public class CashAccountController {

    private final CashAccountService service;

    public CashAccountController(CashAccountService service) {
        this.service = service;
    }

    @GetMapping("/cash_accounts")
    @ApiOperation(value = "API for get all of cash accounts")
    public List<CashAccounts> getAll(){
        return service.getAllAccounts();
    }

    @GetMapping("/cash_accounts/{accountID}")
    @ApiOperation(value = "API for get one of cash account by ID")
    public CashAccounts getOneById (@PathVariable Long accountID){
        return service.getOneAccountByID(accountID);
    }

    @PostMapping("/cash_accounts")
    @ApiOperation(value = "API for post cash account")
    public ResponseMessage createAccounts (@RequestBody CashAccounts cashAccounts){
        service.createAccount(cashAccounts);
        return new ResponseMessage(ResultCode.SUCCESS,"Счет успешно создан");
    }

    @PutMapping("/cash_accounts/{accountID}/name")
    @ApiOperation(value = "API for change cash account name by ID")
    public ResponseMessage changeName (@PathVariable Long accountID,
                                       @RequestBody CashAccounts cashAccounts){
        service.changeAccountNameByID(accountID,cashAccounts);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя счета изменено");
    }

    @PutMapping("/cash_accounts/{accountID}/cash")
    @ApiOperation(value = "API for change cash in account by ID")
    public ResponseMessage changeCash (@PathVariable Long accountID,
                                       @RequestBody CashAccounts cashAccounts){
        service.changeAccountCashByID(accountID,cashAccounts);
        return new ResponseMessage(ResultCode.SUCCESS,"Сумма на счету изменено");
    }
}
