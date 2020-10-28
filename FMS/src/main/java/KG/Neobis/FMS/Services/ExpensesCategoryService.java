package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CategoryForExpenses;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ExpensesCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesCategoryService {

    private final ExpensesCategoryRepository expensesCategoryRepository;

    public ExpensesCategoryService(ExpensesCategoryRepository expensesCategoryRepository) {
        this.expensesCategoryRepository = expensesCategoryRepository;
    }

    public List<CategoryForExpenses> getAllExpenseCategories(){
        return expensesCategoryRepository.findAll();
    }

    public CategoryForExpenses getOneByID(Long expensesCategoryID){
        return expensesCategoryRepository.findById(expensesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions("Такой категории для расходов не найдено"));
    }

    public CategoryForExpenses getOneByName (String categoryName){
        return expensesCategoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    return expensesCategoryRepository.save(new CategoryForExpenses(categoryName));
                });
    }

    public CategoryForExpenses changeNameCategory (CategoryForExpenses newCategory, Long categoryID){
        return expensesCategoryRepository.findById(categoryID)
                .map(category -> {
                    category.setCategoryName(newCategory.getCategoryName());
                    return expensesCategoryRepository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions("Такой категории для доходов не найдено"));
    }
}
