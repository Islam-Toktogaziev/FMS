package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.CashAccountsRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CashAccountService {

    private final CashAccountsRepository repository;

    public CashAccountService(CashAccountsRepository repository) {
        this.repository = repository;
    }

    public List<CashAccounts> getAllAccounts (){
        return repository.findAll();
    }

    public CashAccounts getOneAccountByID(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public CashAccounts getOneAccountsByName(String name){
        return repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public CashAccounts getOneAccountsIDByName(String name){
        return repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public CashAccounts createAccount(CashAccounts cashAccounts){
        if (repository.existsByName(cashAccounts.getName())){
            throw  new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Счет с таким именем уже существует"));
        }
        return repository.save(cashAccounts);
    }

    public boolean noEnoughInCashAccount(CashAccounts cashAccounts, BigDecimal sumOfTransaction){
        if (cashAccounts.getSumInAccount().compareTo(sumOfTransaction) < 0){
            return false;
        }else {
            return true;
        }
    }

    public void changeAccountCashByName(String name, CashAccounts newCashAccounts){
         repository.findByName(name)
                .map(cashAccounts -> {
                    cashAccounts.setSumInAccount(newCashAccounts.getSumInAccount());
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким именем")));
    }

    public void changeAccountNameByID (Long id, CashAccounts newCashAccounts){
        repository.findById(id)
                .map(cashAccounts -> {
                    cashAccounts.setName(newCashAccounts.getName());
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public void changeAccountCashByID (Long id, CashAccounts newCashAccounts){
        repository.findById(id)
                .map(cashAccounts -> {
                    cashAccounts.setSumInAccount(newCashAccounts.getSumInAccount());
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

}
