package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 0", nativeQuery = true)
    BigDecimal getIncomesSum();

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 1", nativeQuery = true)
    BigDecimal getExpensesSum();
}
