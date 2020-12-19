package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long>, JpaSpecificationExecutor<Transactions> {

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 0 and deleted = false and actual_date > now()-interval '1' month ", nativeQuery = true)
    BigDecimal getIncomesSum();

    @Query(value = "select SUM(sum_of_transaction) from transactions where type_of_transaction = 1 and deleted = false and actual_date > now()-interval '1' month", nativeQuery = true)
    BigDecimal getExpensesSum();
}
