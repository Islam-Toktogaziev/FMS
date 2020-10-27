package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TotalSumService {

    private final TransactionRepository transactionRepository;

    public TotalSumService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getSumIncomes(){
        if(transactionRepository.getIncomesSum() == null){
            return new BigDecimal(0);
        }
        return transactionRepository.getIncomesSum();
    }

    public BigDecimal getSumExpenses(){
        if(transactionRepository.getExpensesSum() == null){
            return new BigDecimal(0);
        }
        return transactionRepository.getExpensesSum();
    }


}
