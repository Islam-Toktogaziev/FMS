package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CashAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CashAccountsRepository extends JpaRepository<CashAccounts, Long> {

    Optional<CashAccounts> findByNameAndDeletedFalse (String name);
    boolean existsByNameAndDeletedFalse(String name);

    @Query (value = "select SUM(c.sum_in_account) from cash_accounts c where c.deleted = false",nativeQuery = true)
    BigDecimal getSumInCashAccounts();

    List<CashAccounts> findAllByArchivedFalseAndDeletedFalse();

    List<CashAccounts> findAllByDeletedFalse();
}
