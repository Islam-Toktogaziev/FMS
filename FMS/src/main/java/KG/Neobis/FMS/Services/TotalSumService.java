package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Repositories.CashAccountsRepository;
import KG.Neobis.FMS.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TotalSumService {

    private final TransactionRepository transactionRepository;
    private final CashAccountsRepository cashAccountsRepository;

    public TotalSumService(TransactionRepository transactionRepository,
                           CashAccountsRepository cashAccountsRepository) {
        this.transactionRepository = transactionRepository;
        this.cashAccountsRepository = cashAccountsRepository;
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

    public BigDecimal getSumInCashAccounts(){
        if(cashAccountsRepository.getSumInCashAccounts() == null){
            return new BigDecimal(0);
        }
        return cashAccountsRepository.getSumInCashAccounts();
    }

}
