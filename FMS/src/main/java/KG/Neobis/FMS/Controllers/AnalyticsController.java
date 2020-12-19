package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Services.AnalyticsService;
import KG.Neobis.FMS.dto.AnalyticsDates;
import KG.Neobis.FMS.dto.GraphModel;
import KG.Neobis.FMS.dto.PieChartModel;
import KG.Neobis.FMS.dto.TotalSumsIncomesAndExpenses;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Analytics", description = "APIs for analytics")
public class AnalyticsController {
    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @ApiOperation("API for incomes pie chart")
    @GetMapping("/analytics/incomes_categories")
    public List<PieChartModel> getIncomesForPieCharts (AnalyticsDates dates){
        return service.getIncomesForPieCharts(dates);
    }

    @ApiOperation("API for expenses pie chart")
    @GetMapping("/analytics/expenses_categories")
    public List<PieChartModel> getExpensesForPieCharts (AnalyticsDates dates){
        return service.getExpensesForPieCharts(dates);
    }

    @ApiOperation("API for expenses graphs")
    @GetMapping("/analytics/expenses_graphs")
    public List<GraphModel> getExpensesForGraphs (AnalyticsDates dates){
        return service.getExpensesForGraph(dates);
    }

    @ApiOperation("API for expenses graphs")
    @GetMapping("/analytics/incomes_graphs")
    public List<GraphModel> getIncomesForGraphs (AnalyticsDates dates){
        return service.getIncomesForGraph(dates);
    }

    @ApiOperation(value = "API for get total sum of expenses and incomes")
    @GetMapping("/sums_expenses_and_incomes")
    public TotalSumsIncomesAndExpenses getSumsIncomesAndExpenses(AnalyticsDates dates){
        return service.getSums(dates);
    }
}
