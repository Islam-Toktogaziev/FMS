package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ExpensesCategoryService;
import KG.Neobis.FMS.dto.CategoryModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api (value = "Expenses category")
public class ExpensesCategoryController {

    private final ExpensesCategoryService expensesCategoryService;

    public ExpensesCategoryController(ExpensesCategoryService expensesCategoryService) {
        this.expensesCategoryService = expensesCategoryService;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping ("/admin/expenses_categories")
    @ApiOperation("API for get all of expenses categories")
    public List<ExpensesCategories> getAllOfExpensesCategories (){
        return expensesCategoryService.getAllExpenseCategories();
    }

    @GetMapping ("/expenses_categories/not_archived")
    @ApiOperation("API for get all of categories that are not archived")
    public List<CategoryModel> getAllNotArchived (){
        return expensesCategoryService.getAllNotArchived();
    }

    @GetMapping ("/expenses_categories/not_deleted")
    @ApiOperation("API for get all of categories that are not deleted")
    public List<CategoryModel> getAllNotDeleted (){
        return expensesCategoryService.getAllNotDeleted();
    }

    @ApiOperation("API for get one category by ID")
    @GetMapping ("/expenses_categories/{categoryID}")
    public ExpensesCategories getOneByID (@PathVariable Long categoryID){
        return expensesCategoryService.getOneByID(categoryID);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_категории')")
    @ApiOperation("API for post expense category")
    @PostMapping ("/expenses_categories")
    public ResponseMessage createExpensesCategory (@RequestBody CategoryModel expensesCategories){
        expensesCategoryService.createExpensesCategory(expensesCategories);
        return new ResponseMessage(ResultCode.SUCCESS,"Категория успешно добавлена");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_категории')")
    @ApiOperation("API to change name of category")
    @PutMapping ("/expenses_categories/{categoryID}")
    public ResponseMessage changeNameOfExpensesCategory (@RequestBody CategoryModel newCategory,
                                                            @PathVariable Long categoryID){
        expensesCategoryService.changeNameCategory(newCategory,categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменена");
    }

    @ApiOperation("API to Archive")
    @PutMapping ("/expenses_categories/archive/{categoryID}")
    public ResponseMessage archive (@RequestBody CategoryModel newCategory,
                                                         @PathVariable Long categoryID){
        expensesCategoryService.archive(categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменена");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/admin/expenses_cateogories/delete/{categoryID}")
    @ApiOperation("Delete expense category by ID")
    public ResponseMessage deleteCategory(@PathVariable Long categoryID){
        expensesCategoryService.deleteCategory(categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удален");
    }
}
