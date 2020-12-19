package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Contractors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractorsRepository extends JpaRepository<Contractors, Long> {

    Optional<Contractors> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse(String name);

    List<Contractors> findAllByArchivedFalseAndDeletedFalse();

    List<Contractors> findAllByDeletedFalse();
}
