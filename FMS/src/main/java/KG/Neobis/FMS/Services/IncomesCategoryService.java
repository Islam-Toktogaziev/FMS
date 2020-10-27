package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.CategoryForIncomes;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.IncomesCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomesCategoryService {

    private final IncomesCategoryRepository repository;

    public IncomesCategoryService(IncomesCategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryForIncomes> getAllIncomeCategories(){
        return repository.findAll();
    }

    public CategoryForIncomes getOneByID(Long incomesCategoryID){
        return repository.findById(incomesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions("Такой категории для доходов не найдено"));
    }

    public CategoryForIncomes getOneByName (String categoryName){
        return repository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    return repository.save(new CategoryForIncomes(categoryName));
                });
    }

    public CategoryForIncomes changeNameCategory (CategoryForIncomes newCategory, Long categoryID){
        return repository.findById(categoryID)
                .map(category -> {
                    category.setCategoryName(newCategory.getCategoryName());
                    return repository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions("Такой категории для доходов не найдено"));
    }
}
