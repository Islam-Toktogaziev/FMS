package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.IncomesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.IncomesCategoryService;
import KG.Neobis.FMS.dto.CategoryModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Incomes category")
public class IncomesCategoryController {

    private final IncomesCategoryService incomesCategoryService;

    public IncomesCategoryController(IncomesCategoryService incomesCategoryService) {
        this.incomesCategoryService = incomesCategoryService;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/incomes_categories")
    @ApiOperation(value = "API for get all income categories")
    public List<IncomesCategories> getAll(){
        return incomesCategoryService.getAllIncomeCategories();
    }

    @GetMapping ("/incomes_categories/not_archived")
    @ApiOperation("API for get all of categories that are not archived")
    public List<CategoryModel> getAllNotArchived (){
        return incomesCategoryService.getAllNotArchived();
    }

    @GetMapping ("/incomes_categories/not_deleted")
    @ApiOperation("API for get all of categories that are not deleted")
    public List<CategoryModel> getAllNotDeleted (){
        return incomesCategoryService.getAllNotDeleted();
    }

    @GetMapping("/incomes_categories/{categoryID}")
    @ApiOperation("API for get one category by ID")
    public IncomesCategories getOneIncomesCategoryByID (@PathVariable Long categoryID){
        return incomesCategoryService.getOneByID(categoryID);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_категории')")
    @PostMapping("/incomes_categories")
    @ApiOperation(value = "API for post income category")
    public ResponseMessage createIncome (@RequestBody CategoryModel income){
        incomesCategoryService.createIncomesCategory(income);
        return new ResponseMessage(ResultCode.SUCCESS,"Категория успешно добавлена");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_категории')")
    @PutMapping("/incomes_categories/{categoryID}")
    @ApiOperation("API for change a name of category")
    public ResponseMessage changeName (@RequestBody CategoryModel newCategory,
                                         @PathVariable Long categoryID){
        incomesCategoryService.changeNameCategory(newCategory,categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменено");
    }

    @ApiOperation("API to Archive")
    @PutMapping ("/v/archive/{categoryID}")
    public ResponseMessage archive (
                                    @PathVariable Long categoryID){
        incomesCategoryService.archive(categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя категории успешно изменена");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/admin/incomes_categories/delete/{categoryID}")
    @ApiOperation("Delete income category by ID")
    public ResponseMessage deleteCategory(@PathVariable Long categoryID){
        incomesCategoryService.deleteCategory(categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удален");
    }
}
