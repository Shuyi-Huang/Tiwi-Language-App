package com.example.week_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboard extends AppCompatActivity {

    private Button addTextButton;
    private Button editTextButton;
    private Button viewWordsButton;
    private Button recordVoiceButton; // Declare the recordVoiceButton
    private ImageView backButton; // Declare the backButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        addTextButton = findViewById(R.id.addTextButton);
        editTextButton = findViewById(R.id.editTextButton);
        viewWordsButton = findViewById(R.id.viewWordsButton);
        recordVoiceButton = findViewById(R.id.recordVoiceButton); // Initialize the recordVoiceButton
        backButton = findViewById(R.id.back_button); // Initialize the backButton

        // Add Text Button
        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, AddText.class);
                startActivity(intent);
            }
        });

        // Edit Text Button
        editTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, EditTextHandler.class);
                startActivity(intent);
            }
        });

        // View Words Button
        viewWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, ViewWords.class);
                intent.putExtra("source", "teacher"); // Pass "teacher" as the source
                startActivity(intent);
            }
        });

        // Record Voice Button
        recordVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, VoiceRecordingActivity.class);
                startActivity(intent);
            }
        });

        // Back Button to return to MainActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }
}
