package com.example.week_5;

import android.content.Context;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    // WordPair class to store English and Tiwi phrases
    public static class WordPair {
        private String english;
        private String tiwi;

        public WordPair(String english, String tiwi) {
            this.english = english;
            this.tiwi = tiwi;
        }

        // Getter for English phrase
        public String getEnglish() {
            return english;
        }

        // Getter for Tiwi phrase
        public String getTiwi() {
            return tiwi;
        }
    }

    // Method to read Excel file and return list of WordPairs
    public static List<WordPair> readExcelFile(Context context) {
        List<WordPair> wordPairs = new ArrayList<>();

        try {
            // Open the Excel file from the assets folder
            InputStream is = context.getAssets().open("TiwiList.xlsx");
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over each row in the sheet
            for (Row row : sheet) {
                Cell englishCell = row.getCell(0);
                Cell tiwiCell = row.getCell(1);

                // Get the cell values
                String english = englishCell.getStringCellValue();
                String tiwi = tiwiCell.getStringCellValue();

                // Add a new WordPair to the list
                wordPairs.add(new WordPair(english, tiwi));
            }

            // Close the workbook after reading
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordPairs;  // Return the list of word pairs
    }
}
