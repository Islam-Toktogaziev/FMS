package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.CashAccounts;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ToCashAccountMapper implements RowMapper<CashAccounts> {
    @Override
    public CashAccounts mapRow(ResultSet resultSet, int i) throws SQLException {
        CashAccounts cashAccounts = new CashAccounts();
        cashAccounts.setName(resultSet.getString("to_cash"));
        return cashAccounts;
    }
}
