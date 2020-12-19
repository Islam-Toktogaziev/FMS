package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.TransactionDAO;
import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.TransactionTags;
import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Enums.TypeOfTransaction;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.TransactionRepository;
import KG.Neobis.FMS.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    private final ChangeLogRepository logRepository;

    public TransactionService(TransactionRepository transactionRepository, CashAccountService cashAccountService, ContractorsService contractorsService, IncomesCategoryService incomesCategoryService, ExpensesCategoryService expensesCategoryService, TransactionTagsService transactionTagsService, ProjectService projectService, TransactionDAO transactionDAO, ChangeLogRepository logRepository) {
        this.transactionRepository = transactionRepository;
        this.cashAccountService = cashAccountService;
        this.contractorsService = contractorsService;
        this.incomesCategoryService = incomesCategoryService;
        this.expensesCategoryService = expensesCategoryService;
        this.transactionTagsService = transactionTagsService;
        this.projectService = projectService;
        this.transactionDAO = transactionDAO;
        this.logRepository = logRepository;
    }

    public JournalOfTransaction getAllTransactionNotDeleted(RequestFilter requestFilter){
        if (requestFilter.getPageNumber() == null){
            requestFilter.setPageNumber(new BigInteger("1"));
        }
        if (requestFilter.getTransactionsInPage() == null){
            requestFilter.setTransactionsInPage(new BigInteger("20"));
        }
        if (requestFilter.getTransactionsInPage().compareTo(new BigInteger("0")) <= 0){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Количество элементов на странице должно быть больше 0"));
        }
        if (requestFilter.getPageNumber().compareTo(new BigInteger("0")) <= 0){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Номер страницы должна быть больше 0"));
        }
        BigInteger numberOfPage = transactionDAO.getNumberOfPages(requestFilter);
        if (numberOfPage.mod(requestFilter.getTransactionsInPage()).compareTo(new BigInteger("0")) > 0){
            numberOfPage = numberOfPage.divide(requestFilter.getTransactionsInPage()).add(new BigInteger("1"));
        } else {
            numberOfPage = numberOfPage.divide(requestFilter.getTransactionsInPage());
        }

        return new JournalOfTransaction(responseTransactionList(transactionDAO.findAll(requestFilter)),numberOfPage);
    }

    public List<ResponseTransaction> getTransactionsForExport(RequestFilter requestFilter){
        return responseTransactionList(transactionDAO.findAllForExport(requestFilter));
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
        logRepository.save(new ChangeLog("Добавил транзакцию 'ДОХОД'"));
        transactionRepository.save(transactions);
    }

    public void deleteTransaction(Long id){
        transactionRepository.findById(id)
                .map(transactions -> {
                    logRepository.save(new ChangeLog("Удалил транзакцию"));
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
        logRepository.save(new ChangeLog("Добавил транзакцию 'РАСХОД'"));
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
        logRepository.save(new ChangeLog("Добавил транзакцию 'ПЕРЕВОД'"));
        transactionRepository.save(transactions);
    }

    public void changeTransaction(RequestTransaction newTransaction,Long id){
        transactionRepository.findById(id)
                .map(transactions -> {
                    if (newTransaction.isStatus() != transactions.isActual()){
                        transactions.setActual(newTransaction.isStatus());
                    }
                    if (newTransaction.getSumOfTransaction() != null){
                        transactions.setSumOfTransaction(newTransaction.getSumOfTransaction());
                    }
                    if (newTransaction.getContractor() != null && !newTransaction.getContractor().equalsIgnoreCase("Без контрагента")){
                        transactions.setContractor(contractorsService.getContractorByName(newTransaction.getContractor()));
                    }
                    if (newTransaction.getProject() != null && !newTransaction.getProject().equalsIgnoreCase("Без проекта")){
                        transactions.setProjects(projectService.getOneByName(newTransaction.getProject()));
                    }
                    if (newTransaction.getActualDate() != null && !newTransaction.getActualDate().equals(transactions.getActualDate())){
                        transactions.setActualDate(newTransaction.getActualDate());
                    }
                    if (newTransaction.getTags() != null){
                        transactions.setTags(setNewTags(newTransaction.getTags()));
                    }
                    if (newTransaction.getDescription() != null){
                        transactions.setDescription(newTransaction.getDescription());
                    }
                    if (transactions.getTypeOfTransaction() == TypeOfTransaction.Доход)
                    {
                        if (newTransaction.getCategory() != null && !newTransaction.getCategory().equalsIgnoreCase("Другое")){
                            transactions.setIncomesCategories(incomesCategoryService.getOneByName(newTransaction.getCategory()));
                        }
                        if (newTransaction.getCashAccount() != null){
                            transactions.setToCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
                        }
                    }
                    else if (transactions.getTypeOfTransaction() == TypeOfTransaction.Расход)
                    {
                        if (newTransaction.getCategory() != null && !newTransaction.getCategory().equalsIgnoreCase("Другое")){
                            transactions.setExpensesCategories(expensesCategoryService.getOneByName(newTransaction.getCategory()));
                        }
                        if (newTransaction.getCashAccount() != null){
                            transactions.setFromCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
                        }
                    }
                    else {
                        if (newTransaction.getCashAccount() != null){
                            transactions.setFromCashAccount(cashAccountService.getOneAccountsByName(newTransaction.getCashAccount()));
                        }
                        if (newTransaction.getCategory() != null){
                            String str = newTransaction.getCategory();
                            if (str.startsWith("->")){
                                str = str.substring(2);
                            }
                            transactions.setToCashAccount(cashAccountService.getOneAccountsByName(str));
                        }
                    }
                    logRepository.save(new ChangeLog("Изменил данные транзакцию"));
                    return transactionRepository.save(transactions);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найдено транзакциия с таким ID")));
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

    private List<ResponseTransaction> responseTransactionList(List<Transactions> transactionsFromDB) {
        List<ResponseTransaction> transactions = new ArrayList<>();
        for (Transactions temp : transactionsFromDB) {
            ResponseTransaction newTrans = new ResponseTransaction();
            newTrans.setId(temp.getId());
            newTrans.setActualDate(temp.getActualDate());
            newTrans.setType(temp.getTypeOfTransaction());
            if (temp.getDescription() != null) {
                newTrans.setDescription(temp.getDescription());
            } else {
                newTrans.setDescription("Без описания");
            }
            if (temp.getContractor().getName() != null) {
                newTrans.setContractor(temp.getContractor().getName());
            } else {
                newTrans.setContractor("Без контрагента");
            }

            newTrans.setStatus(temp.isActual());
            Set<String> strTag = new HashSet<>();
            for (TransactionTags tags : transactionRepository.findById(temp.getId()).get().getTags()
            ) {
                strTag.add(tags.getName());
            }
            if (strTag.isEmpty()) {
                strTag.add("Без тегов");
            }
            newTrans.setTags(strTag);

            if (temp.getProjects().getName() != null) {
                newTrans.setProject(temp.getProjects().getName());
            } else {
                newTrans.setProject("Без проекта");
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

}
