package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.IncomesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.IncomesCategoryRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomesCategoryService {

    private final IncomesCategoryRepository repository;

    public IncomesCategoryService(IncomesCategoryRepository repository) {
        this.repository = repository;
    }

    public List<IncomesCategories> getAllIncomeCategories(){
        return repository.findAll();
    }

    public IncomesCategories getOneByID(Long incomesCategoryID){
        return repository.findById(incomesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    public IncomesCategories createIncomesCategory (IncomesCategories newCategory){
        return repository.save(newCategory);
    }

    public IncomesCategories getOneByName (String categoryName){
        return repository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    public IncomesCategories changeNameCategory (IncomesCategories newCategory, Long categoryID){
        return repository.findById(categoryID)
                .map(category -> {
                    category.setCategoryName(newCategory.getCategoryName());
                    return repository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }
}
