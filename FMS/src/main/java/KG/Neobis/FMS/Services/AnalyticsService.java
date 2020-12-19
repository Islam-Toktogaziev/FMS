package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.AnalyticsDAO;
import KG.Neobis.FMS.dto.AnalyticsDates;
import KG.Neobis.FMS.dto.GraphModel;
import KG.Neobis.FMS.dto.PieChartModel;
import KG.Neobis.FMS.dto.TotalSumsIncomesAndExpenses;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsDAO analyticsDAO;

    public AnalyticsService(AnalyticsDAO analyticsDAO) {
        this.analyticsDAO = analyticsDAO;
    }

    public List<PieChartModel> getIncomesForPieCharts(AnalyticsDates dates){
        setDates(dates);
        return analyticsDAO.getIncomesForPieChart(dates);
    }

    public List<PieChartModel> getExpensesForPieCharts(AnalyticsDates dates){
        setDates(dates);
        return analyticsDAO.getExpensesForPieChart(dates);
    }

    public List<GraphModel> getIncomesForGraph(AnalyticsDates dates){
        setDates(dates);
        return analyticsDAO.getIncomesForGraph(dates);
    }

    public List<GraphModel> getExpensesForGraph(AnalyticsDates dates){
        setDates(dates);
        return analyticsDAO.getExpensesForGraph(dates);
    }

    public TotalSumsIncomesAndExpenses getSums(AnalyticsDates dates){
        setDates(dates);
        BigDecimal incomes = analyticsDAO.getIncomesSum(dates);
        if (incomes == null){
            incomes = new BigDecimal(0);
        }
        BigDecimal expenses = analyticsDAO.getExpensesSum(dates);
        if (expenses == null){
            expenses = new BigDecimal(0);
        }
        return new TotalSumsIncomesAndExpenses(incomes,expenses);
    }

    private void setDates(AnalyticsDates dates){
        if (dates.getToDate() == null){
            dates.setToDate(LocalDate.now());
        }
        if (dates.getFromDate() == null){
            dates.setFromDate(dates.getToDate().minusMonths(1));
        }
        if (dates.getFromDate() != null && dates.getToDate() != null
                && dates.getToDate().isBefore(dates.getFromDate())){
            LocalDate temp = dates.getFromDate();
            dates.setFromDate(dates.getToDate());
            dates.setToDate(temp);
        }
    }
}
