package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.CategoryForIncomes;
import KG.Neobis.FMS.Repositories.IncomesCategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "Incomes category")
public class IncomesCategoryController {

    private final IncomesCategoryRepository repository;

    public IncomesCategoryController(IncomesCategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/incomes_categories")
    @ApiOperation(value = "API for get all income categories")
    public List<CategoryForIncomes> getAll(){
        return repository.findAll();
    }

    @PostMapping("/incomes_categories")
    @ApiOperation(value = "API for post income category")
    public CategoryForIncomes createIncome (@RequestBody CategoryForIncomes income){
        return repository.save(income);
    }
}
