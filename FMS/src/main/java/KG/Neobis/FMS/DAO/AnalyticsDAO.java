package KG.Neobis.FMS.DAO;

import KG.Neobis.FMS.DAO.Mappers.GraphMapper;
import KG.Neobis.FMS.DAO.Mappers.PieChartMapper;
import KG.Neobis.FMS.dto.AnalyticsDates;
import KG.Neobis.FMS.dto.GraphModel;
import KG.Neobis.FMS.dto.PieChartModel;
import KG.Neobis.FMS.dto.RequestFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public class AnalyticsDAO {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PieChartModel> getIncomesForPieChart(AnalyticsDates dates) {
        String sql = "select ic.category_name, sum(t.sum_of_transaction) from transactions t \n" +
                "left join incomes_categories ic on ic.id = t.incomes_category\n" +
                "where t.deleted = false and t.type_of_transaction = 0 and t.actual_date >= '"+dates.getFromDate()+"' and t.actual_date <= '"+dates.getToDate()+"'\n" +
                "group by ic.category_name;";

        return jdbcTemplate.query(
                sql,
                new PieChartMapper());
    }

    public List<PieChartModel> getExpensesForPieChart(AnalyticsDates dates) {
        String sql = "select ex.category_name, sum(t.sum_of_transaction) from transactions t\n" +
                "left join expenses_categories ex on ex.id = t.expenses_category\n" +
                "where t.deleted = false and type_of_transaction = 1 and t.actual_date >= '"+dates.getFromDate()+"' and t.actual_date <= '"+dates.getToDate()+"'\n" +
                "group by ex.category_name;";

        return jdbcTemplate.query(
                sql,
                new PieChartMapper());
    }

    public List<GraphModel> getIncomesForGraph(AnalyticsDates dates) {
        String sql = "select date(t.actual_date) as act_date, sum(t.sum_of_transaction) from transactions t\n" +
                "where t.deleted = false and t.type_of_transaction = 0 and t.actual_date >= '"+dates.getFromDate()+"' and t.actual_date <= '"+dates.getToDate()+"'\n" +
                "group by act_date order by act_date;";

        return jdbcTemplate.query(
                sql,
                new GraphMapper());
    }

    public List<GraphModel> getExpensesForGraph(AnalyticsDates dates) {
        String sql = "select date(t.actual_date) as act_date, sum(t.sum_of_transaction) from transactions t\n" +
                "where t.deleted = false and t.type_of_transaction = 1 and t.actual_date >= '"+dates.getFromDate()+"' and t.actual_date <= '"+dates.getToDate()+"'\n" +
                "group by act_date order by act_date;";

        return jdbcTemplate.query(
                sql,
                new GraphMapper());
    }

    public BigDecimal getIncomesSum (AnalyticsDates dates){
        String sql = "select SUM(t.sum_of_transaction) from transactions t where t.type_of_transaction = 0 and t.deleted = false and t.actual_date >= '" +dates.getFromDate()+ "' and t.actual_date <= '" + dates.getToDate() + "'";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{},
                BigDecimal.class
        );
    }

    public BigDecimal getExpensesSum (AnalyticsDates dates){
        String sql = "select SUM(t.sum_of_transaction) from transactions t where t.type_of_transaction = 1 and t.deleted = false and t.actual_date >= '" +dates.getFromDate()+ "' and t.actual_date <= '" + dates.getToDate() + "'";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{},
                BigDecimal.class
        );
    }

}
