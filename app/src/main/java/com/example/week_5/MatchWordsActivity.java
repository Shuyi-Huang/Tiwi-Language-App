package com.example.week_5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.List;

public class MatchWordsActivity extends AppCompatActivity {

    private LinearLayout wordsLayout, meaningsLayout;
    private TextView selectedWordTextView = null;
    private TextView selectedMeaningTextView = null;
    private int matchedPairsCount = 0;  // Counter for matched pairs
    private int totalPairs = 8;  // Total number of pairs to match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_words);

        // Initialize layouts
        wordsLayout = findViewById(R.id.wordsLayout);
        meaningsLayout = findViewById(R.id.meaningsLayout);

        // Add back button functionality to return to StudentDashboard
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MatchWordsActivity.this, StudentDashboard.class);
            startActivity(intent);
            finish();
        });

        // Load words from assets
        List<ExcelReader.WordPair> wordPairs = ExcelReader.readExcelFile(this);

        // We will only take 8 word pairs for the game
        if (wordPairs.size() > 8) {
            wordPairs = wordPairs.subList(0, 8);  // Correct usage: start from 0 and end at 8
        }

        // Shuffle the lists so that the words and meanings are not in the same order
        Collections.shuffle(wordPairs);

        // Populate the UI with words and their meanings
        populateWordsAndMeanings(wordPairs);
    }

    private void populateWordsAndMeanings(List<ExcelReader.WordPair> wordPairs) {
        for (ExcelReader.WordPair wordPair : wordPairs) {
            // Create Tiwi word TextView
            TextView tiwiWordTextView = new TextView(this);
            tiwiWordTextView.setText(wordPair.getTiwi());
            tiwiWordTextView.setTag(wordPair.getTiwi());  // Use the Tiwi phrase as the tag
            tiwiWordTextView.setPadding(26, 26, 26, 26);
            tiwiWordTextView.setBackgroundResource(R.drawable.word_background); // Customize background

            tiwiWordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Select the Tiwi word
                    selectedWordTextView = tiwiWordTextView;
                    checkMatch();
                }
            });

            wordsLayout.addView(tiwiWordTextView);

            // Create English meaning TextView
            TextView englishMeaningTextView = new TextView(this);
            englishMeaningTextView.setText(wordPair.getEnglish());
            englishMeaningTextView.setTag(wordPair.getTiwi());  // Use the same Tiwi tag for matching
            englishMeaningTextView.setPadding(26, 26, 126, 26);
            englishMeaningTextView.setBackgroundResource(R.drawable.word_background);

            englishMeaningTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Select the English meaning
                    selectedMeaningTextView = englishMeaningTextView;
                    checkMatch();
                }
            });

            meaningsLayout.addView(englishMeaningTextView);
        }
    }

    // Check if the selected Tiwi word matches the selected English meaning
    private void checkMatch() {
        if (selectedWordTextView != null && selectedMeaningTextView != null) {
            String selectedWordTag = selectedWordTextView.getTag().toString();
            String selectedMeaningTag = selectedMeaningTextView.getTag().toString();

            // If the tags match, hide both TextViews
            if (selectedWordTag.equals(selectedMeaningTag)) {
                Toast.makeText(this, "Correct Match!", Toast.LENGTH_SHORT).show();
                selectedWordTextView.setVisibility(View.GONE);
                selectedMeaningTextView.setVisibility(View.GONE);

                matchedPairsCount++;  // Increment the counter for matched pairs
                Log.d("MatchWords", "Matched Pairs: " + matchedPairsCount);

                // Check if all pairs are matched
                if (matchedPairsCount == totalPairs) {
                    Log.d("MatchWords", "All pairs matched! Displaying message.");
                    showCompletionMessage();
                }
            } else {
                Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            }

            // Reset selection
            selectedWordTextView = null;
            selectedMeaningTextView = null;
        }
    }

    // Show the success page when all pairs are matched
    private void showCompletionMessage() {
        Intent intent = new Intent(MatchWordsActivity.this, SuccessPageActivity.class);
        startActivity(intent);
    }
}
