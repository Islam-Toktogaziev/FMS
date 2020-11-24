package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CashAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface CashAccountsRepository extends JpaRepository<CashAccounts, Long> {

    Optional<CashAccounts> findByName (String name);
    boolean existsByName(String name);

    @Query (value = "select SUM(sum_in_account) from cash_accounts",nativeQuery = true)
    BigDecimal getSumInCashAccounts();
}
