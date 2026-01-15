package com.example.week_5;

import android.content.ClipData;
import android.content.Intent;  // Add the missing import for Intent
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Collections;
import java.util.List;

public class MatchWordActivity2 extends AppCompatActivity {

    private LinearLayout wordsLayout, meaningsLayout;
    private int matchesMade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_word_2);

        // Initialize layouts
        wordsLayout = findViewById(R.id.wordsLayout);
        meaningsLayout = findViewById(R.id.meaningsLayout);

        // Load words from assets using a helper function (WordListReader)
        List<WordListReader.WordPair> wordPairs = WordListReader.readWordList(this);

        // Use the rest of the words from the list for this activity
        List<WordListReader.WordPair> remainingWordPairs = wordPairs.subList(8, wordPairs.size());  // Adjust index based on earlier activity
        Collections.shuffle(remainingWordPairs);  // Shuffle the list for disordered style

        // Skip the first 5 pairs (for example, if they were used in the previous activity)
        remainingWordPairs = wordPairs.subList(1, wordPairs.size());


        // Shuffle the remaining word pairs to display them in a disordered manner
        Collections.shuffle(remainingWordPairs);

        // Populate the game UI
        populateWordsAndMeanings(remainingWordPairs);
    }

    // Populate the game with the remaining words
    private void populateWordsAndMeanings(List<WordListReader.WordPair> wordPairs) {
        for (WordListReader.WordPair wordPair : wordPairs) {
            // Create Tiwi word TextView (draggable)
            TextView tiwiWordTextView = new TextView(this);
            tiwiWordTextView.setText(wordPair.getTiwiWord());
            tiwiWordTextView.setTag(wordPair.getTiwiWord());
            tiwiWordTextView.setPadding(16, 26, 16, 26);
            tiwiWordTextView.setBackgroundResource(R.drawable.word_background);
            tiwiWordTextView.setOnLongClickListener(view -> {
                ClipData data = ClipData.newPlainText("drag", view.getTag().toString());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            });

            // Add Tiwi word to the layout
            wordsLayout.addView(tiwiWordTextView);

            // Create English meaning TextView (drop target)
            TextView meaningTextView = new TextView(this);
            meaningTextView.setText(wordPair.getEnglishMeaning());
            meaningTextView.setTag(wordPair.getTiwiWord());  // Use Tiwi word as tag for matching
            meaningTextView.setPadding(16, 16, 16, 16);
            meaningTextView.setBackgroundResource(R.drawable.word_background);
            meaningTextView.setOnDragListener((view, event) -> handleDrop(view, event));

            // Add meaning to the layout
            meaningsLayout.addView(meaningTextView);
        }
    }

    // Handle the drop event for matching words
    private boolean handleDrop(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                String draggedWord = event.getClipData().getItemAt(0).getText().toString();
                String targetTag = view.getTag().toString();

                if (draggedWord.equals(targetTag)) {
                    // Correct match
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer));  // Correct match color
                    view.setVisibility(View.GONE);  // Hide the matched word
                    matchesMade++;

                    // Check if all matches are made
                    if (matchesMade == 8) {  // Set this to the total number of pairs to match
                        showCompletionMessage();
                    }
                } else {
                    // Incorrect match
                    Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    // Show a message when all pairs are matched
    private void showCompletionMessage() {
        Intent intent = new Intent(MatchWordActivity2.this, SuccessPageActivity.class);
        intent.putExtra("message", "Amazing! Keep Moving!");
        startActivity(intent);
    }
}
