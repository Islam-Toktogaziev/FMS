package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.DeleteMethods;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.ExpensesCategories;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.ExpensesCategoryRepository;
import KG.Neobis.FMS.dto.CategoryModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpensesCategoryService {

    private final ExpensesCategoryRepository expensesCategoryRepository;
    private final DeleteMethods deleteMethods;
    private final ChangeLogRepository logRepository;

    public ExpensesCategoryService(ExpensesCategoryRepository expensesCategoryRepository, DeleteMethods deleteMethods, ChangeLogRepository logRepository) {
        this.expensesCategoryRepository = expensesCategoryRepository;
        this.deleteMethods = deleteMethods;
        this.logRepository = logRepository;
    }

    public List<ExpensesCategories> getAllExpenseCategories(){
        return expensesCategoryRepository.findAll();
    }

    public List<CategoryModel> getAllNotArchived(){
        List<ExpensesCategories> categoriesFromDB = expensesCategoryRepository.findAllByDeletedFalseAndArchivedFalse();
        List<CategoryModel> categories = new ArrayList<>();
        for (ExpensesCategories temp:categoriesFromDB
             ) {
            categories.add(serialize(temp));
        }
        return categories;
    }

    public List<CategoryModel> getAllNotDeleted(){
        List<ExpensesCategories> categoriesFromDB = expensesCategoryRepository.findAllByDeletedFalse();
        List<CategoryModel> categories = new ArrayList<>();
        for (ExpensesCategories temp:categoriesFromDB
        ) {
            categories.add(serialize(temp));
        }
        return categories;
    }

    public ExpensesCategories getOneByID(Long expensesCategoryID){
        return expensesCategoryRepository.findById(expensesCategoryID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public ExpensesCategories createExpensesCategory (CategoryModel newCategory){
        if (expensesCategoryRepository.existsByCategoryNameAndDeletedFalse(newCategory.getCategoryName())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Категория с таким именем уже существует"));
        }
        ExpensesCategories expensesCategories = new ExpensesCategories();
        expensesCategories.setCategoryName(newCategory.getCategoryName());
        expensesCategories.setDeleted(false);
        expensesCategories.setArchived(false);
        logRepository.save(new ChangeLog("Создал категорию для расходов '" + expensesCategories.getCategoryName() + "'"));
        return expensesCategoryRepository.save(expensesCategories);
    }

    public ExpensesCategories getOneByName (String categoryName){
        return expensesCategoryRepository.findByCategoryNameAndDeletedFalse(categoryName)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public ExpensesCategories changeNameCategory (CategoryModel newCategory, Long categoryID){
        return expensesCategoryRepository.findById(categoryID)
                .map(category -> {
                    if (expensesCategoryRepository.existsByCategoryNameAndDeletedFalse(newCategory.getCategoryName()) && !categoryID.equals(category.getId())){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Категория с таким именем уже существует"));
                    }
                    if (!category.getCategoryName().equals(newCategory.getCategoryName())){
                        logRepository.save(new ChangeLog("Сменил название категории расхода '" + category.getCategoryName() + "' на '" + newCategory.getCategoryName() + "'"));
                        category.setCategoryName(newCategory.getCategoryName());
                    }
                    if (!category.isArchived() && newCategory.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал категория расходов '" + category.getCategoryName() + "'"));
                        category.setArchived(newCategory.isArchived());
                    }
                    if (category.isArchived() && !newCategory.isArchived()){
                        logRepository.save(new ChangeLog("Разархивировал категория расходов '" + category.getCategoryName() + "'"));
                        category.setArchived(newCategory.isArchived());
                    }
                    return expensesCategoryRepository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public ExpensesCategories archive (Long categoryID){
        return expensesCategoryRepository.findById(categoryID)
                .map(category -> {
                    if (!category.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал категория расходов '" + category.getCategoryName() + "'"));
                        category.setArchived(true);
                        return expensesCategoryRepository.save(category);
                    }
                    if (category.isArchived()){
                        logRepository.save(new ChangeLog("Разархивировал категория расходов '" + category.getCategoryName() + "'"));
                        category.setArchived(false);
                        return expensesCategoryRepository.save(category);
                    }
                    return expensesCategoryRepository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    public void deleteCategory(Long categoryID){
        expensesCategoryRepository.findById(categoryID)
                .map(expensesCategories -> {
                    deleteMethods.deleteExpenseCategory(categoryID);
                    expensesCategories.setDeleted(true);
                    logRepository.save(new ChangeLog("Удалил категорию расхода '" + expensesCategories.getCategoryName() + "'"));
                    return expensesCategoryRepository.save(expensesCategories);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Такой категории для расходов не найдено")));
    }

    private CategoryModel serialize(ExpensesCategories temp){
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryName(temp.getCategoryName());
        categoryModel.setArchived(temp.isArchived());
        categoryModel.setId(temp.getId());
        return categoryModel;
    }
}
