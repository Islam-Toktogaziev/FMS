package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.IncomesCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomesCategoryRepository extends JpaRepository<IncomesCategories,Long>{
    Optional<IncomesCategories> findByCategoryName (String categoryName);
}