package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpensesCategoriesMapper implements RowMapper<ExpensesCategories> {
    @Override
    public ExpensesCategories mapRow(ResultSet resultSet, int i) throws SQLException {
        ExpensesCategories categories = new ExpensesCategories();
        categories.setCategoryName(resultSet.getString("expense"));
        return categories;
    }
}
