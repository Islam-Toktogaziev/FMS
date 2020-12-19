package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.dto.ResponseTransaction;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExcelExporter {

    public static ByteArrayInputStream contactListToExcelFile(List<ResponseTransaction> transactions) {
        try(
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                XSSFWorkbook workbook = new XSSFWorkbook();
            ){
            XSSFSheet sheet = workbook.createSheet("Transactions");

            int rowNum = 0;
            Cell cell;
            Row row;

            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            String date;

            row = sheet.createRow(0);
            XSSFCellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("ID");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Дата");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Сумма транзакции");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Тип транзакции");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Счет");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Категория");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("Контрагент");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue("Проект");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue("Описание");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(9, CellType.STRING);
            cell.setCellValue("Тэги");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each customer
            for (ResponseTransaction temp: transactions
                 ) {
                rowNum++;
                row = sheet.createRow(rowNum);

                cell = row.createCell(0,CellType.STRING);
                cell.setCellValue(temp.getId());

                cell = row.createCell(1);
                date = dateFormatter.format(temp.getActualDate());
                cell.setCellValue(date);

                cell = row.createCell(2,CellType.NUMERIC);
                cell.setCellValue(String.valueOf(temp.getSumOfTransaction()));

                cell = row.createCell(3,CellType.STRING);
                cell.setCellValue(String.valueOf(temp.getType()));

                cell = row.createCell(4,CellType.STRING);
                cell.setCellValue(temp.getCashAccount());

                cell = row.createCell(5,CellType.STRING);
                cell.setCellValue(temp.getCategory());

                cell = row.createCell(6,CellType.STRING);
                cell.setCellValue(temp.getContractor());

                cell = row.createCell(7,CellType.STRING);
                cell.setCellValue(temp.getProject());

                cell = row.createCell(8,CellType.STRING);
                cell.setCellValue(temp.getDescription());

                cell = row.createCell(9,CellType.STRING);
                cell.setCellValue(temp.getTags().toString());

            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);

            workbook.write(outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
