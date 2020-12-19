package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.Contractors;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractorMapper implements RowMapper<Contractors> {

    @Override
    public Contractors mapRow(ResultSet resultSet, int i) throws SQLException {
        Contractors contractors = new Contractors();
        contractors.setName(resultSet.getString("c_name"));
        return contractors;
    }
}
