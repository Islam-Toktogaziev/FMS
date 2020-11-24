package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.CashAccounts;
import KG.Neobis.FMS.Entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long>, JpaSpecificationExecutor<Transactions> {

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 0 and deleted = false", nativeQuery = true)
    BigDecimal getIncomesSum();

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 1 and deleted = false", nativeQuery = true)
    BigDecimal getExpensesSum();

    List<Transactions> findAllByActualDateGreaterThanEqualAndActualDateLessThanEqual (Date date1, Date date2);

    List<Transactions> findAllByToCashAccountOrFromCashAccount (CashAccounts toCashAccount, CashAccounts fromCashAccount);

    List<Transactions> findAllByActualDateGreaterThanEqual (Date date);

    List<Transactions> findAllByActualDateLessThanEqual (Date date);
}
