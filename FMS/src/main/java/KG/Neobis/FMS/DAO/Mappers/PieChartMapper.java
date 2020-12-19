package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.dto.PieChartModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PieChartMapper implements RowMapper<PieChartModel> {

    @Override
    public PieChartModel mapRow(ResultSet resultSet, int i) throws SQLException {
        PieChartModel model = new PieChartModel();
        model.setName(resultSet.getString(1));
        model.setSum(resultSet.getBigDecimal(2));
        return model;
    }
}
