package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.IncomesCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomesCategoryRepository extends JpaRepository<IncomesCategories,Long>{
    Optional<IncomesCategories> findByCategoryNameAndDeletedFalse (String categoryName);

    List<IncomesCategories> findAllByDeletedFalseAndArchivedFalse();

    List<IncomesCategories> findAllByDeletedFalse();

    boolean existsByCategoryNameAndDeletedFalse(String name);
}