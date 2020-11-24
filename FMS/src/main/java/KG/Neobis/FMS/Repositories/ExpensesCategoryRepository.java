package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.ExpensesCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpensesCategoryRepository extends JpaRepository<ExpensesCategories,Long> {

    Optional<ExpensesCategories> findByCategoryName (String categoryName);
}
