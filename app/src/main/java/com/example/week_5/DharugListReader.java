package com.example.week_5;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DharugListReader {

    public List<String[]> readWordsFromExcel(InputStream inputStream) {
        List<String[]> words = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String[] rowData = new String[row.getLastCellNum()];
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i] = cell.getStringCellValue();
                }
                words.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }
}
