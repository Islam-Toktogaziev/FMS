package KG.Neobis.FMS.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class RequestFilter {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date toDate;

    private String cashAccount;

    private String contractor;

    private String typeOfTransaction;

    private String project;

    private BigInteger pageNumber;

    private BigInteger transactionsInPage;
}
