package com.example.week_5;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordListReader {

    // Reads the CSV file from assets and returns a list of WordPairs
    public static List<WordPair> readWordList(Context context) {
        List<WordPair> wordPairs = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("TiwiList.csv"); // Ensure this file exists in the assets folder
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean isFirstLine = true; // Variable to skip the first line

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Skip the header row
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 2) {  // Ensure each row contains at least 2 columns
                    String tiwiWord = data[0].trim();
                    String englishMeaning = data[1].trim();
                    wordPairs.add(new WordPair(tiwiWord, englishMeaning));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordPairs;
    }

    // Class to represent the word pairs
    public static class WordPair {
        private final String englishMeaning;
        private final String tiwiWord;


        public WordPair(String tiwiWord, String englishMeaning) {
            this.tiwiWord = tiwiWord;
            this.englishMeaning = englishMeaning;
        }

        public String getTiwiWord() {
            return tiwiWord;
        }

        public String getEnglishMeaning() {
            return englishMeaning;
        }
    }
}
