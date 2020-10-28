package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Exceptions.ResourceAlreadyExists;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.CashAccountsRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.sql.SQLIntegrityConstraintViolationException;
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
                .orElseThrow(() -> new ResourceNotFoundExceptions("Не найдено счета с таким ID"));
    }

    public CashAccounts getOneAccountsByName(String name){
        return repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions("Счет с таким имененем не найден"));
    }

    public CashAccounts createAccount(CashAccounts cashAccounts){
        return repository.save(cashAccounts);
    }

    public void changeAccountCash (String name, CashAccounts newCashAccounts){
         repository.findByName(name)
                .map(cashAccounts -> {
                    cashAccounts.setSumInAccount(newCashAccounts.getSumInAccount());
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions("Не найдено счета с таким именем"));
    }

}
