package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.TransactionTags;
import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.Entities.TypeOfTransaction;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.TransactionRepository;
import KG.Neobis.FMS.dto.RequestTransaction;
import KG.Neobis.FMS.dto.ResponseTransaction;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CashAccountService cashAccountService;
    private final ContractorsService contractorsService;
    private final IncomesCategoryService incomesCategoryService;
    private final ExpensesCategoryService expensesCategoryService;
    private final TransactionTagsService transactionTagsService;

    public TransactionService(TransactionRepository transactionRepository,
                              CashAccountService cashAccountService,
                              ContractorsService contractorsService,
                              IncomesCategoryService incomesCategoryService,
                              ExpensesCategoryService expensesCategoryService,
                              TransactionTagsService transactionTagsService) {
        this.transactionRepository = transactionRepository;
        this.cashAccountService = cashAccountService;
        this.contractorsService = contractorsService;
        this.incomesCategoryService = incomesCategoryService;
        this.expensesCategoryService = expensesCategoryService;
        this.transactionTagsService = transactionTagsService;
    }

    public List<ResponseTransaction> getAllTransactionNotDeleted() {
        List<Transactions> transactionsFromDB = transactionRepository.findAll();
        List<ResponseTransaction> transactions = new ArrayList<>();
        for (Transactions temp : transactionsFromDB) {
            if (!temp.isDeleted()){
                ResponseTransaction newTrans = new ResponseTransaction();
                newTrans.setId(temp.getId());
                newTrans.setActualDate(temp.getActualDate());
                newTrans.setType(temp.getTypeOfTransaction());
                newTrans.setDescription(temp.getDescription());
                newTrans.setContractor(temp.getContractor().getName());
                newTrans.setStatus(temp.isActual());
                newTrans.setTags(temp.getTags());
                newTrans.setSumOfTransaction(temp.getSumOfTransaction());
                if (newTrans.getType() == TypeOfTransaction.Доход) {
                    newTrans.setCategory(temp.getCategoryForIncomes().getCategoryName());
                    newTrans.setCashAccount(temp.getToCashAccount().getName());
                }
                if (newTrans.getType() == TypeOfTransaction.Расход) {
                    newTrans.setCategory(temp.getCategoryForExpenses().getCategoryName());
                    newTrans.setCashAccount(temp.getFromCashAccount().getName());
                }
                if (newTrans.getType() == TypeOfTransaction.Перевод) {
                    newTrans.setCategory("->" + temp.getToCashAccount().getName());
                    newTrans.setCashAccount(temp.getFromCashAccount().getName());
                }
                transactions.add(newTrans);
            }
        }
        return transactions;
    }

    public void createIncomeTransaction(RequestTransaction newTransaction){
        Transactions transactions = new Transactions();
        transactions.setTypeOfTransaction(TypeOfTransaction.Доход);
        transactions.setActual(newTransaction.isStatus());
        transactions.setCategoryForIncomes(incomesCategoryService.getOneByName(newTransaction.getCategory()));
        transactions.setActualDate(newTransaction.getActualDate());
        transactions.setContractor(contractorsService.getContractorByName(newTransaction.getContractor()));
        transactions.setToCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
        transactions.setDescription(newTransaction.getDescription());
        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
        transactions.setDeleted(false);
        cashAccountService.changeAccountCash(newTransaction.getCashAccount(),new CashAccounts(transactions.getToCashAccount().getSumInAccount().add(transactions.getSumOfTransaction())));
        if (newTransaction.getTags() != null) {
            Set<String> tagsInString = new HashSet<>(Arrays.asList(newTransaction.getTags().split(" ")));
            Set<TransactionTags> newTags = new HashSet<>();
            for (String tags : tagsInString) {
                newTags.add(transactionTagsService.getTagByName(tags));
            }
            transactions.setTags(newTags);
        }
        transactionRepository.save(transactions);
    }

    public void deleteTransaction(Long id){
        transactionRepository.findById(id)
                .map(transactions -> {
                    transactions.setDeleted(true);
                    return transactionRepository.save(transactions);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions("Не найдено транзакциия с таким ID"));
    }

    public void createExpenseTransaction(RequestTransaction newTransaction){
        Transactions transactions = new Transactions();
        transactions.setTypeOfTransaction(TypeOfTransaction.Расход);
        transactions.setActual(newTransaction.isStatus());
        transactions.setCategoryForExpenses(expensesCategoryService.getOneByName(newTransaction.getCategory()));
        transactions.setActualDate(newTransaction.getActualDate());
        transactions.setContractor(contractorsService.getContractorByName(newTransaction.getContractor()));
        transactions.setFromCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
        transactions.setDescription(newTransaction.getDescription());
        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
        transactions.setDeleted(false);
        cashAccountService.changeAccountCash(newTransaction.getCashAccount(),new CashAccounts(transactions.getFromCashAccount().getSumInAccount().subtract(transactions.getSumOfTransaction())));
        if (newTransaction.getTags() != null) {
            Set<String> tagsInString = new HashSet<>(Arrays.asList(newTransaction.getTags().split(" ")));
            Set<TransactionTags> newTags = new HashSet<>();
            for (String tags : tagsInString) {
                newTags.add(transactionTagsService.getTagByName(tags));
            }
            transactions.setTags(newTags);
        }
        transactionRepository.save(transactions);
    }

}
