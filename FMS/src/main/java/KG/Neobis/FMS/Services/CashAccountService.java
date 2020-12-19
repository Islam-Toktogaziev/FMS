package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.CashAccountsRepository;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.dto.CashAccountModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import KG.Neobis.FMS.dto.SumInCashAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CashAccountService {

    private final CashAccountsRepository repository;
    private final ChangeLogRepository logRepository;

    public CashAccountService(CashAccountsRepository repository, ChangeLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    public List<CashAccounts> getAllAccounts (){
        return repository.findAll();
    }

    public List<CashAccountModel> getAllNotArchived(){
        List<CashAccounts> cashAccounts = repository.findAllByArchivedFalseAndDeletedFalse();
        List<CashAccountModel> model = new ArrayList<>();
        for (CashAccounts temp: cashAccounts) {
            model.add(serialize(temp));
        }
        return model;
    }

    public List<CashAccountModel> getAllNotDeleted(){
        List<CashAccounts> cashAccounts = repository.findAllByDeletedFalse();
        List<CashAccountModel> model = new ArrayList<>();
        for (CashAccounts temp: cashAccounts) {
            model.add(serialize(temp));
        }
        return model;
    }

    public CashAccounts getOneAccountByID(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public CashAccounts getOneAccountsByName(String name){
        return repository.findByNameAndDeletedFalse(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public CashAccounts createAccount(CashAccountModel cashAccounts){
        if (repository.existsByNameAndDeletedFalse(cashAccounts.getName())){
            throw  new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Счет с таким именем уже существует"));
        }
        CashAccounts newAccount = new CashAccounts(cashAccounts.getName(),new BigDecimal("0"));
        newAccount.setDeleted(false);
        newAccount.setArchived(false);
        logRepository.save(new ChangeLog("Создал счет с названием '" + newAccount.getName() + "'"));
        return repository.save(newAccount);
    }

    public boolean noEnoughInCashAccount(CashAccounts cashAccounts, BigDecimal sumOfTransaction){
        if (cashAccounts.getSumInAccount().compareTo(sumOfTransaction) < 0){
            return false;
        }else {
            return true;
        }
    }

    public void changeAccountCashByName(String name, CashAccounts newCashAccounts){
         repository.findByNameAndDeletedFalse(name)
                .map(cashAccounts -> {
                    cashAccounts.setSumInAccount(newCashAccounts.getSumInAccount());
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким именем")));
    }

    public void changeAccountDataByID(Long id, CashAccountModel newCashAccounts){
        repository.findById(id)
                .map(cashAccounts -> {
                    if (repository.existsByNameAndDeletedFalse(newCashAccounts.getName()) && !id.equals(cashAccounts.getId())){
                        throw  new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Счет с таким именем уже существует"));
                    }
                    if (!cashAccounts.getName().equals(newCashAccounts.getName())){
                        logRepository.save(new ChangeLog("Название счета '" + cashAccounts.getName() + "' изменил на '" + newCashAccounts.getName() + "'"));
                        cashAccounts.setName(newCashAccounts.getName());
                    }
                    if (!cashAccounts.isArchived() && newCashAccounts.isArchived()){
                        cashAccounts.setArchived(newCashAccounts.isArchived());
                        logRepository.save(new ChangeLog("Заархивировал счет '" + cashAccounts.getName() + "'"));
                    }
                    if (cashAccounts.isArchived() && !newCashAccounts.isArchived()){
                        cashAccounts.setArchived(newCashAccounts.isArchived());
                        logRepository.save(new ChangeLog("Разархивировал счет '" + cashAccounts.getName() + "'"));
                    }
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public void archive(Long id){
        repository.findById(id)
                .map(cashAccounts -> {
                    if (!cashAccounts.isArchived()){
                        cashAccounts.setArchived(true);
                        logRepository.save(new ChangeLog("Заархивировал счет '" + cashAccounts.getName() + "'"));
                        return repository.save(cashAccounts);
                    }
                    if (cashAccounts.isArchived()){
                        cashAccounts.setArchived(false);
                        logRepository.save(new ChangeLog("Разархивировал счет '" + cashAccounts.getName() + "'"));
                        return repository.save(cashAccounts);
                    }
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public void deleteCashAccount(Long id){
        repository.findById(id)
                .map(cashAccounts -> {
                    if (cashAccounts.getSumInAccount().compareTo(new BigDecimal(0)) != 0){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION, "Удалить счет можно только с нулевым значением!"));
                    }
                    cashAccounts.setDeleted(true);
                    logRepository.save(new ChangeLog("Удалил счет '" + cashAccounts.getName() + "'"));
                    return repository.save(cashAccounts);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено счета с таким ID")));
    }

    public SumInCashAccount getSumInCashAccounts(){
        BigDecimal sum = repository.getSumInCashAccounts();
        if (sum == null){
            sum = new BigDecimal(0);
        }
        return new SumInCashAccount(sum);
    }

    private CashAccountModel serialize(CashAccounts temp){
        CashAccountModel accountModel = new CashAccountModel();
        accountModel.setName(temp.getName());
        accountModel.setSumInAccount(temp.getSumInAccount());
        accountModel.setArchived(temp.isArchived());
        accountModel.setId(temp.getId());
        return accountModel;
    }

}
