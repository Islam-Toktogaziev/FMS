package KG.Neobis.FMS.DAO.Mappers;

import KG.Neobis.FMS.Entities.Projects;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapper implements RowMapper<Projects> {
    @Override
    public Projects mapRow(ResultSet resultSet, int i) throws SQLException {
        Projects projects = new Projects();
        projects.setName(resultSet.getString("p_name"));
        return projects;
    }
}
