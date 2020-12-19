package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.Transactions;
import KG.Neobis.FMS.Enums.TypeOfTransaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements RowMapper<Transactions> {

    @Override
    public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException {

        Transactions transactions = new Transactions();
        transactions.setId(rs.getLong("id"));
        transactions.setDescription(rs.getString("description"));
        transactions.setSumOfTransaction(rs.getBigDecimal("sum_of_transaction"));
        transactions.setDeleted(rs.getBoolean("deleted"));
        transactions.setActual(rs.getBoolean("is_actual"));
        transactions.setActualDate(rs.getTimestamp("actual_date"));
        transactions.setCreatedAt(rs.getTimestamp("created_at"));
        transactions.setUpdatedAt(rs.getTimestamp("updated_at"));
        transactions.setCreatedBy(rs.getString("created_by"));
        transactions.setUpdatedBy(rs.getString("updated_by"));
        transactions.setTypeOfTransaction(TypeOfTransaction.values()[rs.getInt("type_of_transaction")]);
        transactions.setContractor(new ContractorMapper().mapRow(rs, rowNum));
        transactions.setFromCashAccount(new FromCashAccountMapper().mapRow(rs, rowNum));
        transactions.setToCashAccount(new ToCashAccountMapper().mapRow(rs, rowNum));
        transactions.setIncomesCategories(new IncomesCategoriesMapper().mapRow(rs, rowNum));
        transactions.setExpensesCategories(new ExpensesCategoriesMapper().mapRow(rs, rowNum));
        transactions.setProjects(new ProjectMapper().mapRow(rs, rowNum));

        return transactions;

    }
}
