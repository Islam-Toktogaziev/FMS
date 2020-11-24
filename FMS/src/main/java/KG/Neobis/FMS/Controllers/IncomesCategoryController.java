package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.IncomesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.IncomesCategoryService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Incomes category")
public class IncomesCategoryController {

    private final IncomesCategoryService incomesCategoryService;

    public IncomesCategoryController(IncomesCategoryService incomesCategoryService) {
        this.incomesCategoryService = incomesCategoryService;
    }

    @GetMapping("/incomes_categories")
    @ApiOperation(value = "API for get all income categories")
    public List<IncomesCategories> getAll(){
        return incomesCategoryService.getAllIncomeCategories();
    }

    @GetMapping("/incomes_categories/{categoryID}")
    @ApiOperation("API for get one category by ID")
    public IncomesCategories getOneIncomesCategoryByID (@PathVariable Long categoryID){
        return incomesCategoryService.getOneByID(categoryID);
    }

    @PostMapping("/incomes_categories")
    @ApiOperation(value = "API for post income category")
    public ResponseMessage createIncome (@RequestBody IncomesCategories income){
        incomesCategoryService.createIncomesCategory(income);
        return new ResponseMessage(ResultCode.SUCCESS,"Категория успешно добавлена");
    }

    @PutMapping("/incomes_categories/{categoryID}")
    @ApiOperation("API for change a name of category")
    public ResponseMessage changeName (@RequestBody IncomesCategories newCategory,
                                         @PathVariable Long categoryID){
        incomesCategoryService.changeNameCategory(newCategory,categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменено");
    }
}
