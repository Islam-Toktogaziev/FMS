package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Services.ExcelExporter;
import KG.Neobis.FMS.Services.TransactionService;
import KG.Neobis.FMS.dto.RequestFilter;
import KG.Neobis.FMS.dto.ResponseTransaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Export excel")
public class ExportExcel {

    private final TransactionService transactionService;

    public ExportExcel(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation("API for export to Excel")
    @GetMapping("/transactions/export")
    public void downloadCsv(HttpServletResponse response, RequestFilter filter) throws IOException, Exception {
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateTime = dateFormatter.format(new Date());

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=transaction_" + currentDateTime + ".xlsx");

            List<ResponseTransaction> transactionList = transactionService.getTransactionsForExport(filter);

            ByteArrayInputStream stream = ExcelExporter.contactListToExcelFile(transactionList);

        IOUtils.copy(stream, response.getOutputStream());
    }
}
