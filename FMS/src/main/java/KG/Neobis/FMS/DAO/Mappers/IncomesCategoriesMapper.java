package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.IncomesCategories;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncomesCategoriesMapper implements RowMapper<IncomesCategories> {
    @Override
    public IncomesCategories mapRow(ResultSet resultSet, int i) throws SQLException {
        IncomesCategories categories = new IncomesCategories();
        categories.setCategoryName(resultSet.getString("income"));
        return categories;
    }
}
