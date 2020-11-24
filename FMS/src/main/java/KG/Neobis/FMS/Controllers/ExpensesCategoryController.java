package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ExpensesCategoryService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api (value = "Expenses category")
public class ExpensesCategoryController {

    private final ExpensesCategoryService expensesCategoryService;

    public ExpensesCategoryController(ExpensesCategoryService expensesCategoryService) {
        this.expensesCategoryService = expensesCategoryService;
    }

    @GetMapping ("/expenses_categories")
    @ApiOperation("API for get all of expenses categories")
    public List<ExpensesCategories> getAllOfExpensesCategories (){
        return expensesCategoryService.getAllExpenseCategories();
    }

    @ApiOperation("API for get one category by ID")
    @GetMapping ("/expenses_categories/{categoryID}")
    public ExpensesCategories getOneByID (@PathVariable Long categoryID){
        return expensesCategoryService.getOneByID(categoryID);
    }

    @ApiOperation("API for post expense category")
    @PostMapping ("/expenses_categories")
    public ResponseMessage createExpensesCategory (@RequestBody ExpensesCategories expensesCategories){
        expensesCategoryService.createExpensesCategory(expensesCategories);
        return new ResponseMessage(ResultCode.SUCCESS,"Категория успешно добавлена");
    }

    @ApiOperation("API to change name of category")
    @PutMapping ("/expenses_categories/{categoryID}")
    public ResponseMessage changeNameOfExpensesCategory (@RequestBody ExpensesCategories newCategory,
                                                            @PathVariable Long categoryID){
        expensesCategoryService.changeNameCategory(newCategory,categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменена");
    }
}
