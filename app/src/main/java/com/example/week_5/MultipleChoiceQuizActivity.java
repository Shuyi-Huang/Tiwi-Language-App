package com.example.week_5;
import com.example.week_5.ExcelReader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipleChoiceQuizActivity extends AppCompatActivity {

    private TextView tiwiPhraseTextView;
    private Button optionButton1, optionButton2, optionButton3;
    private ImageView backButton;  // Back button to return to StudentDashboard

    private List<ExcelReader.WordPair> wordPairList;
    private ExcelReader.WordPair currentWordPair;
    private int currentQuestionIndex = 0;
    private int totalQuestions = 5;  // Number of questions in the quiz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_quiz);

        // Bind views
        tiwiPhraseTextView = findViewById(R.id.tiwiPhraseTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        backButton = findViewById(R.id.back_button);  // Back button

        // Back button logic: navigate to StudentDashboard when clicked
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MultipleChoiceQuizActivity.this, StudentDashboard.class);
            startActivity(intent);
            finish();
        });

        // Load words from the Excel file using ExcelReader
        wordPairList = ExcelReader.readExcelFile(this);

        // Shuffle the list to get random questions
        Collections.shuffle(wordPairList);

        // Ensure we only take 5 random questions from the list
        if (wordPairList.size() > totalQuestions) {
            wordPairList = wordPairList.subList(0, totalQuestions);
        }

        // Start the quiz with the first question
        displayNextQuestion();
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            // Get the current word pair
            currentWordPair = wordPairList.get(currentQuestionIndex);

            // Display the Tiwi phrase
            tiwiPhraseTextView.setText(currentWordPair.getTiwi());

            // Create a list of options (including the correct answer)
            List<String> options = new ArrayList<>();
            options.add(currentWordPair.getEnglish());

            // Add 2 dummy options from the list randomly
            Random random = new Random();
            while (options.size() < 3) {
                int randomIndex = random.nextInt(wordPairList.size());
                String dummyOption = wordPairList.get(randomIndex).getEnglish();
                if (!options.contains(dummyOption)) {
                    options.add(dummyOption);
                }
            }

            // Shuffle the options
            Collections.shuffle(options);

            // Set the options on the buttons
            optionButton1.setText(options.get(0));
            optionButton2.setText(options.get(1));
            optionButton3.setText(options.get(2));

            // Set click listeners for each option
            optionButton1.setOnClickListener(v -> checkAnswer(optionButton1.getText().toString()));
            optionButton2.setOnClickListener(v -> checkAnswer(optionButton2.getText().toString()));
            optionButton3.setOnClickListener(v -> checkAnswer(optionButton3.getText().toString()));
        } else {
            showCompletionMessage();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(currentWordPair.getEnglish())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong Answer, Try Again!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        displayNextQuestion();
    }

    private void showCompletionMessage() {
        // Create an AlertDialog to give users the option to "Try Again" or "Exit"
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Completed!");
        builder.setMessage("Would you like to try again or exit?");

        // Add "Try Again" button
        builder.setPositiveButton("Try Again", (dialog, which) -> {
            // Reset the quiz to the beginning
            currentQuestionIndex = 0;
            Collections.shuffle(wordPairList); // Reshuffle the questions
            displayNextQuestion();  // Start quiz from the first question
        });

        // Add "Exit" button
        builder.setNegativeButton("Exit", (dialog, which) -> {
            // Navigate back to the StudentDashboard
            Intent intent = new Intent(MultipleChoiceQuizActivity.this, StudentDashboard.class);
            startActivity(intent);
            finish();  // Close the quiz activity
        });

        // Show the AlertDialog
        builder.show();
    }
}
