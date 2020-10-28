package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CategoryForExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpensesCategoryRepository extends JpaRepository<CategoryForExpenses,Long> {

    Optional<CategoryForExpenses> findByCategoryName (String categoryName);
}
