package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CashAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashAccountsRepository extends JpaRepository<CashAccounts, Long> {

    Optional<CashAccounts> findByName (String name);
    boolean existsByName(String name);
}
