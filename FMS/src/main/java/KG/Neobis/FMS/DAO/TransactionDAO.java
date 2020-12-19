package KG.Neobis.FMS.DAO;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Entities.Projects;
import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.DAO.Mappers.TransactionMapper;
import KG.Neobis.FMS.Services.*;
import KG.Neobis.FMS.dto.RequestFilter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class TransactionDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CashAccountService cashAccountService;
    private final ContractorsService contractorsService;
    private final IncomesCategoryService incomesCategoryService;
    private final ExpensesCategoryService expensesCategoryService;
    private final TransactionTagsService transactionTagsService;
    private final ProjectService projectService;

    public TransactionDAO(JdbcTemplate jdbcTemplate, CashAccountService cashAccountService, ContractorsService contractorsService, IncomesCategoryService incomesCategoryService, ExpensesCategoryService expensesCategoryService, TransactionTagsService transactionTagsService, ProjectService projectService) {
        this.jdbcTemplate = jdbcTemplate;
        this.cashAccountService = cashAccountService;
        this.contractorsService = contractorsService;
        this.incomesCategoryService = incomesCategoryService;
        this.expensesCategoryService = expensesCategoryService;
        this.transactionTagsService = transactionTagsService;
        this.projectService = projectService;
    }

    public List<Transactions> findAll(RequestFilter filter) {
        BigInteger page = filter.getPageNumber().subtract(new BigInteger("1"));
        BigInteger offsetElements = page.multiply(filter.getTransactionsInPage());

        String paging = "order by t.id desc limit " + filter.getTransactionsInPage() + " offset " + offsetElements;

        String sql = "select *, c.name as c_name, fromCa.name as from_cash, toCa.name as to_cash," +
                "income.category_name as income, expense.category_name as expense, p.name as p_name from transactions t\n" +
                "left outer join contractors c on t.contractor = c.id\n" +
                "left join cash_accounts fromCa on fromCa.id = t.from_cash_account\n" +
                "left join cash_accounts toCa on toCa.id = t.to_cash_account\n" +
                "left join incomes_categories income on t.incomes_category = income.id\n" +
                "left join expenses_categories expense on t.expenses_category = expense.id\n" +
                "left join projects p on t.projects = p.id " + createFilter(filter) + paging;

        return jdbcTemplate.query(
                sql,
                new TransactionMapper());
    }

    public List<Transactions> findAllForExport(RequestFilter filter) {
        String sql = "select *, c.name as c_name, fromCa.name as from_cash, toCa.name as to_cash," +
                "income.category_name as income, expense.category_name as expense, p.name as p_name from transactions t\n" +
                "left outer join contractors c on t.contractor = c.id\n" +
                "left join cash_accounts fromCa on fromCa.id = t.from_cash_account\n" +
                "left join cash_accounts toCa on toCa.id = t.to_cash_account\n" +
                "left join incomes_categories income on t.incomes_category = income.id\n" +
                "left join expenses_categories expense on t.expenses_category = expense.id\n" +
                "left join projects p on t.projects = p.id " + createFilter(filter) + " order by t.id desc";

        return jdbcTemplate.query(
                sql,
                new TransactionMapper());
    }

    public BigInteger getNumberOfPages (RequestFilter filter){
        String sql = "select count(*) from transactions t\n" +
                "    left outer join contractors c on t.contractor = c.id\n" +
                "    left join cash_accounts fromCa on fromCa.id = t.from_cash_account\n" +
                "    left join cash_accounts toCa on toCa.id = t.to_cash_account\n" +
                "    left join incomes_categories income on t.incomes_category = income.id\n" +
                "    left join expenses_categories expense on t.expenses_category = expense.id\n" +
                "    left join projects p on t.projects = p.id " + createFilter(filter);
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{},
                BigInteger.class
        );
    }

    private String createFilter(RequestFilter filter){
        boolean and = true;
        String sqlFilter = " where t.deleted = false ";

        if (filter.getContractor() != null){
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            Contractors contractors = contractorsService.getContractorByName(filter.getContractor());
            sqlFilter += "t.contractor = " + contractors.getId() + " ";
            and = true;
        }
        if (filter.getCashAccount() != null){
            CashAccounts cashAccounts = cashAccountService.getOneAccountsByName(filter.getCashAccount());
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            sqlFilter += "(t.to_cash_account = " + cashAccounts.getId() + " or t.from_cash_account = " + cashAccounts.getId() +") ";
            and = true;
        }
        if (filter.getTypeOfTransaction() != null){
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            if (filter.getTypeOfTransaction().equalsIgnoreCase("доход")){
                sqlFilter += "t.type_of_transaction = 0 ";
                and = true;
            }
            if (filter.getTypeOfTransaction().equalsIgnoreCase("расход")){
                sqlFilter += "t.type_of_transaction = 1 ";
                and = true;
            }
            if (filter.getTypeOfTransaction().equalsIgnoreCase("перевод")){
                sqlFilter += "t.type_of_transaction = 2 ";
                and = true;
            }
        }
        if (filter.getFromDate() != null && filter.getToDate() != null){
            if (filter.getToDate().before(filter.getFromDate())){
                Date temp = filter.getFromDate();
                filter.setFromDate(filter.getToDate());
                filter.setToDate(temp);
            }
        }
        if (filter.getProject() != null){
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            Projects projects = projectService.getOneByName(filter.getProject());
            sqlFilter += "t.projects = " + projects.getId() + " ";
            and = true;
        }
        if (filter.getFromDate() != null){
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            sqlFilter += "t.actual_date >= '" + filter.getFromDate() + "' ";
            and = true;
        }
        if (filter.getToDate() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(filter.getToDate());
            calendar.add(Calendar.HOUR_OF_DAY,6);
            if (and){
                sqlFilter += " and ";
                and = false;
            }
            sqlFilter += "t.actual_date <= '" + calendar.getTime() + "' ";
        }
        return sqlFilter;
    }
}
