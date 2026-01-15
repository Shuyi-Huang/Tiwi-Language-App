package com.example.week_5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboard extends AppCompatActivity {

    // Declare buttons and progress text
    private Button viewWordsButton, listeningExerciseButton, gamesButton, quizButton, freedomWallButton;
    private TextView progressTracker;
    private ImageView backButton; // Declare the back button
    private int progress = 0;  // Initialize progress
    private String userName;    // To hold the student's name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);  // Connect to XML layout

        // Retrieve the student's name from SharedPreferences (if stored during login)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "Unknown Student");  // Default to "Unknown Student" if not found

        // Initialize buttons and text view from the layout
        viewWordsButton = findViewById(R.id.viewWordsButton);
        listeningExerciseButton = findViewById(R.id.listeningExerciseButton);
        gamesButton = findViewById(R.id.gamesButton);
        quizButton = findViewById(R.id.quizButton);
        freedomWallButton = findViewById(R.id.freedomWallButton);  // Initialize the Freedom Wall button
        progressTracker = findViewById(R.id.progressTracker);
        backButton = findViewById(R.id.back_button); // Initialize the back button

        // Set onClickListeners for buttons
        viewWordsButton.setOnClickListener(v -> {
            // Start the ViewWordsActivity and indicate that it's coming from the student dashboard
            Intent intent = new Intent(StudentDashboard.this, ViewWords.class);
            intent.putExtra("source", "student"); // Pass "student" as the source
            startActivity(intent);
        });

        listeningExerciseButton.setOnClickListener(v -> {
            // Start the ListeningExerciseActivity
            Intent intent = new Intent(StudentDashboard.this, ListeningExerciseActivity.class);
            startActivity(intent);
        });

        gamesButton.setOnClickListener(v -> {
            // Navigate to the MatchWordsActivity when the Games button is clicked
            Intent intent = new Intent(StudentDashboard.this, MatchWordsActivity.class);
            startActivity(intent);
        });

        quizButton.setOnClickListener(v -> {
            // Start the Quiz activity
            Intent intent = new Intent(StudentDashboard.this, MultipleChoiceQuizActivity.class);
            startActivity(intent);
        });

        // Freedom Wall button functionality
        freedomWallButton.setOnClickListener(v -> {
            // Navigate to the FreedomWallActivity and pass the user's name
            Intent intent = new Intent(StudentDashboard.this, FreedomWallActivity.class);
            intent.putExtra("userName", userName);  // Pass the user's name to FreedomWallActivity
            startActivity(intent);
        });

        // Set onClickListener for the back button
        backButton.setOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(StudentDashboard.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Finish this activity if you don't want it on the back stack
        });

        // Update the progress dynamically (example call)
        updateProgress(10);  // Increment progress by 10% as an example
    }

    // Method to update progress
    private void updateProgress(int increment) {
        progress += increment;  // Increase progress
        progressTracker.setText("Progress: " + progress + "%");  // Update TextView

        // Display a toast message if the student reaches 100% progress
        if (progress >= 100) {
            Toast.makeText(this, "Congratulations! You've completed all tasks!", Toast.LENGTH_LONG).show();
        }
    }
}
