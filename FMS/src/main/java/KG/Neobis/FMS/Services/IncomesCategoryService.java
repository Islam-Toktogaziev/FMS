package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.DeleteMethods;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.IncomesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.IncomesCategoryRepository;
import KG.Neobis.FMS.dto.CategoryModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncomesCategoryService {

    private final IncomesCategoryRepository repository;
    private final DeleteMethods deleteMethods;
    private final ChangeLogRepository logRepository;

    public IncomesCategoryService(IncomesCategoryRepository repository, DeleteMethods deleteMethods, ChangeLogRepository logRepository) {
        this.repository = repository;
        this.deleteMethods = deleteMethods;
        this.logRepository = logRepository;
    }

    public List<IncomesCategories> getAllIncomeCategories(){
        return repository.findAll();
    }

    public List<CategoryModel> getAllNotArchived(){
        List<IncomesCategories> categoriesFromDB = repository.findAllByDeletedFalseAndArchivedFalse();
        List<CategoryModel> model = new ArrayList<>();
        for (IncomesCategories temp: categoriesFromDB
             ) {
            model.add(serialize(temp));
        }
        return model;
    }

    public List<CategoryModel> getAllNotDeleted(){
        List<IncomesCategories> categoriesFromDB = repository.findAllByDeletedFalse();
        List<CategoryModel> model = new ArrayList<>();
        for (IncomesCategories temp: categoriesFromDB
        ) {
            model.add(serialize(temp));
        }
        return model;
    }

    public IncomesCategories getOneByID(Long incomesCategoryID){
        return repository.findById(incomesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    public IncomesCategories createIncomesCategory (CategoryModel newCategory){
        if (repository.existsByCategoryNameAndDeletedFalse(newCategory.getCategoryName())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Категория с таким именем уже существует"));
        }
        IncomesCategories incomesCategories = new IncomesCategories();
        incomesCategories.setCategoryName(newCategory.getCategoryName());
        incomesCategories.setDeleted(false);
        incomesCategories.setArchived(false);
        logRepository.save(new ChangeLog("Создал категорию дохода '" + incomesCategories.getCategoryName() + "'"));
        return repository.save(incomesCategories);
    }

    public IncomesCategories getOneByName (String categoryName){
        return repository.findByCategoryNameAndDeletedFalse(categoryName)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    public IncomesCategories changeNameCategory (CategoryModel newCategory, Long categoryID){
        return repository.findById(categoryID)
                .map(category -> {
                    if (repository.existsByCategoryNameAndDeletedFalse(newCategory.getCategoryName()) && !categoryID.equals(category.getId())){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Категория с таким именем уже существует"));
                    }
                    if (!category.getCategoryName().equals(newCategory.getCategoryName())){
                        logRepository.save(new ChangeLog("Сменил название категории дохода '" + category.getCategoryName() + "' на '" + newCategory.getCategoryName() + "'"));
                        category.setCategoryName(newCategory.getCategoryName());
                    }
                    if (!category.isArchived() && newCategory.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал категорию дохода '" + category.getCategoryName() + "'"));
                        category.setArchived(newCategory.isArchived());
                    }
                    if (category.isArchived() && !newCategory.isArchived()){
                        logRepository.save(new ChangeLog("Разархивировал категорию дохода '" + category.getCategoryName() + "'"));
                        category.setArchived(newCategory.isArchived());
                    }
                    return repository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    public IncomesCategories archive (Long categoryID){
        return repository.findById(categoryID)
                .map(category -> {
                    if (!category.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал категория дохода '" + category.getCategoryName() + "'"));
                        category.setArchived(true);
                        return repository.save(category);
                    }
                    if (category.isArchived()){
                        logRepository.save(new ChangeLog("Разархивировал категория дохода '" + category.getCategoryName() + "'"));
                        category.setArchived(false);
                        return repository.save(category);
                    }
                    return repository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public void deleteCategory(Long categoryID){
        repository.findById(categoryID)
                .map(incomesCategories -> {
                    deleteMethods.deleteIncomeCategory(categoryID);
                    incomesCategories.setDeleted(true);
                    logRepository.save(new ChangeLog("Удалил категорию дохода '" + incomesCategories.getCategoryName() + "'"));
                    return repository.save(incomesCategories);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для доходов не найдено")));
    }

    private CategoryModel serialize(IncomesCategories temp){
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryName(temp.getCategoryName());
        categoryModel.setArchived(temp.isArchived());
        categoryModel.setId(temp.getId());
        return categoryModel;
    }
}
