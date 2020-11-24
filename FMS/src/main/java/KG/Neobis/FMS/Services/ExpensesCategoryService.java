package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ExpensesCategoryRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesCategoryService {

    private final ExpensesCategoryRepository expensesCategoryRepository;

    public ExpensesCategoryService(ExpensesCategoryRepository expensesCategoryRepository) {
        this.expensesCategoryRepository = expensesCategoryRepository;
    }

    public List<ExpensesCategories> getAllExpenseCategories(){
        return expensesCategoryRepository.findAll();
    }

    public ExpensesCategories getOneByID(Long expensesCategoryID){
        return expensesCategoryRepository.findById(expensesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public ExpensesCategories createExpensesCategory (ExpensesCategories newCategory){
        return expensesCategoryRepository.save(newCategory);
    }

    public ExpensesCategories getOneByName (String categoryName){
        return expensesCategoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public ExpensesCategories changeNameCategory (ExpensesCategories newCategory, Long categoryID){
        return expensesCategoryRepository.findById(categoryID)
                .map(category -> {
                    category.setCategoryName(newCategory.getCategoryName());
                    return expensesCategoryRepository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }
}
