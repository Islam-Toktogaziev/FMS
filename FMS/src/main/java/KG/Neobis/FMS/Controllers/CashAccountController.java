package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Services.CashAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    public CashAccounts createAccounts (@RequestBody CashAccounts cashAccounts){
        return service.createAccount(cashAccounts);
    }
}
