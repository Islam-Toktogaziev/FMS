package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CategoryForIncomes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomesCategoryRepository extends JpaRepository<CategoryForIncomes,Long>{
    Optional<CategoryForIncomes> findByCategoryName (String categoryName);
}