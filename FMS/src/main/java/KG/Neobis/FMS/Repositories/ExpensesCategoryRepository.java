package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesCategoryRepository extends JpaRepository<ExpensesCategories,Long> {

    Optional<ExpensesCategories> findByCategoryNameAndDeletedFalse (String categoryName);

    List<ExpensesCategories> findAllByDeletedFalseAndArchivedFalse();

    List<ExpensesCategories> findAllByDeletedFalse();

    boolean existsByCategoryNameAndDeletedFalse(String name);
}
