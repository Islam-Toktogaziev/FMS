package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.TransactionTags;
import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Enums.TypeOfTransaction;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.DAO.TransactionDAO;
import KG.Neobis.FMS.Repositories.TransactionRepository;
import KG.Neobis.FMS.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CashAccountService cashAccountService;
    private final ContractorsService contractorsService;
    private final IncomesCategoryService incomesCategoryService;
    private final ExpensesCategoryService expensesCategoryService;
    private final TransactionTagsService transactionTagsService;
    private final ProjectService projectService;
    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionRepository transactionRepository, CashAccountService cashAccountService, ContractorsService contractorsService, IncomesCategoryService incomesCategoryService, ExpensesCategoryService expensesCategoryService, TransactionTagsService transactionTagsService, ProjectService projectService, TransactionDAO transactionDAO) {
        this.transactionRepository = transactionRepository;
        this.cashAccountService = cashAccountService;
        this.contractorsService = contractorsService;
        this.incomesCategoryService = incomesCategoryService;
        this.expensesCategoryService = expensesCategoryService;
        this.transactionTagsService = transactionTagsService;
        this.projectService = projectService;
        this.transactionDAO = transactionDAO;
    }

    public List<ResponseTransaction> getAllTransactionNotDeleted(RequestFilter requestFilter){
        List<Transactions> transactionsFromDB = transactionDAO.findAll(requestFilter);
        List<ResponseTransaction> transactions = new ArrayList<>();
        for (Transactions temp : transactionsFromDB) {
            ResponseTransaction newTrans = new ResponseTransaction();
            newTrans.setId(temp.getId());
            newTrans.setActualDate(temp.getActualDate());
            newTrans.setType(temp.getTypeOfTransaction());
            if (temp.getDescription() != null){
                newTrans.setDescription(temp.getDescription());
            } else {
                newTrans.setDescription("-");
            }
            if (temp.getContractor().getName() != null){
                newTrans.setContractor(temp.getContractor().getName());
            } else {
                newTrans.setContractor("-");
            }
            newTrans.setStatus(temp.isActual());
            if (temp.getTags() != null){
                newTrans.setTags(temp.getTags());
            } else {
                newTrans.setProject("-");
            }
            if (temp.getProjects().getName() != null) {
                System.out.println(temp.getProjects().toString());
                newTrans.setProject(temp.getProjects().getName());
            } else {
                newTrans.setProject("-");
            }
            newTrans.setSumOfTransaction(temp.getSumOfTransaction());
            if (newTrans.getType() == TypeOfTransaction.Доход) {
                newTrans.setCategory(temp.getIncomesCategories().getCategoryName());
                newTrans.setCashAccount(temp.getToCashAccount().getName());
            }
            if (newTrans.getType() == TypeOfTransaction.Расход) {
                newTrans.setCategory(temp.getExpensesCategories().getCategoryName());
                newTrans.setCashAccount(temp.getFromCashAccount().getName());
            }
            if (newTrans.getType() == TypeOfTransaction.Перевод) {
                newTrans.setCategory("->" + temp.getToCashAccount().getName());
                newTrans.setCashAccount(temp.getFromCashAccount().getName());
            }
            transactions.add(newTrans);
        }
        return transactions;
    }

    public void createIncomeTransaction(RequestTransaction newTransaction) {
        checkNullFields(newTransaction.getSumOfTransaction(),
                newTransaction.getActualDate(),
                newTransaction.getCashAccount(),
                newTransaction.getCategory());
        Transactions transactions = new Transactions();
        transactions.setTypeOfTransaction(TypeOfTransaction.Доход);
        transactions.setActual(newTransaction.isStatus());
        transactions.setIncomesCategories(incomesCategoryService.getOneByName(newTransaction.getCategory()));
        transactions.setActualDate(newTransaction.getActualDate());
        if (newTransaction.getContractor() != null){
            transactions.setContractor(contractorsService.getContractorByName(newTransaction.getContractor()));
        }
        transactions.setToCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
        if (newTransaction.getProject() != null){
            transactions.setProjects(projectService.getOneByName(newTransaction.getProject()));
        }
        transactions.setDescription(newTransaction.getDescription());
        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
        transactions.setDeleted(false);
        cashAccountService.changeAccountCashByName(newTransaction.getCashAccount(), new CashAccounts(transactions.getToCashAccount().getSumInAccount().add(transactions.getSumOfTransaction())));
        if (newTransaction.getTags() != null){
            transactions.setTags(setNewTags(newTransaction.getTags()));
        }
        transactionRepository.save(transactions);
    }

    public void deleteTransaction(Long id){
        transactionRepository.findById(id)
                .map(transactions -> {
                    transactions.setDeleted(true);
                    return transactionRepository.save(transactions);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено транзакциия с таким ID")));
    }

    public void createExpenseTransaction(RequestTransaction newTransaction) {
        checkNullFields(newTransaction.getSumOfTransaction(),
                newTransaction.getActualDate(),
                newTransaction.getCashAccount(),
                newTransaction.getCategory());
        Transactions transactions = new Transactions();
        transactions.setTypeOfTransaction(TypeOfTransaction.Расход);
        transactions.setActual(newTransaction.isStatus());
        transactions.setExpensesCategories(expensesCategoryService.getOneByName(newTransaction.getCategory()));
        transactions.setActualDate(newTransaction.getActualDate());
        if (newTransaction.getContractor() != null){
            transactions.setContractor(contractorsService.getContractorByName(newTransaction.getContractor()));
        }        transactions.setFromCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
        if (!cashAccountService.noEnoughInCashAccount(transactions.getFromCashAccount(), newTransaction.getSumOfTransaction())) {
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Не хватает средств на счету"));
        }
        transactions.setDescription(newTransaction.getDescription());
        if (newTransaction.getProject() != null){
            transactions.setProjects(projectService.getOneByName(newTransaction.getProject()));
        }
        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
        transactions.setDeleted(false);
        cashAccountService.changeAccountCashByName(newTransaction.getCashAccount(), new CashAccounts(transactions.getFromCashAccount().getSumInAccount().subtract(transactions.getSumOfTransaction())));
        if (newTransaction.getTags() != null){
            transactions.setTags(setNewTags(newTransaction.getTags()));
        }
        transactionRepository.save(transactions);
    }

    public void createTransferTransaction(RequestTransfer newTransaction){
        checkNullFields(newTransaction.getSumOfTransaction(),
                newTransaction.getActualDate(),
                newTransaction.getFromCashAccount(),
                newTransaction.getToCashAccount());
        Transactions transactions = new Transactions();
        transactions.setTypeOfTransaction(TypeOfTransaction.Перевод);
        transactions.setActualDate(newTransaction.getActualDate());
        transactions.setFromCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getFromCashAccount()));
        if (!cashAccountService.noEnoughInCashAccount(transactions.getFromCashAccount(),newTransaction.getSumOfTransaction())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Не хватает средств на счету"));
        }
        transactions.setToCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getToCashAccount()));
        transactions.setDescription(newTransaction.getDescription());
        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
        transactions.setDeleted(false);
        cashAccountService.changeAccountCashByName(newTransaction.getToCashAccount(),new CashAccounts(transactions.getToCashAccount().getSumInAccount().add(transactions.getSumOfTransaction())));
        cashAccountService.changeAccountCashByName(newTransaction.getFromCashAccount(),new CashAccounts(transactions.getFromCashAccount().getSumInAccount().subtract(transactions.getSumOfTransaction())));
        if (newTransaction.getTags() != null){
            transactions.setTags(setNewTags(newTransaction.getTags()));
        }
        transactionRepository.save(transactions);
    }

    private Set<TransactionTags> setNewTags(String newTransactionTags){
        Set<String> tagsInString = new HashSet<>(Arrays.asList(newTransactionTags.split(" ")));
        Set<TransactionTags> newTags = new HashSet<>();
        for (String tags : tagsInString) {
            newTags.add(transactionTagsService.getTagByName(tags));
        }
        return newTags;
    }

    private void checkNullFields (BigDecimal sumOfTransaction, Date actualDate,
                                  String cashAccount, String cashAccOrCategory){
        if (sumOfTransaction == null || actualDate == null || cashAccount == null || cashAccOrCategory == null) {
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Заполните объязательные поля"));
        }
    }

}
