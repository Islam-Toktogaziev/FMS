package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.dto.GraphModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GraphMapper implements RowMapper<GraphModel> {

    @Override
    public GraphModel mapRow(ResultSet resultSet, int i) throws SQLException {
        GraphModel model = new GraphModel();
        model.setDate(resultSet.getDate(1));
        model.setSumOfTransaction(resultSet.getBigDecimal(2));
        return model;
    }
}
