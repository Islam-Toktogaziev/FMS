package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Contractors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractorsRepository extends JpaRepository<Contractors, Long> {

    Optional<Contractors> findByName(String name);
}
