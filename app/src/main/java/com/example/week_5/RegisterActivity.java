package com.example.week_5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText newUsername;
    private EditText newPassword;
    private EditText teacherCodeEditText;  // Add this line for teacher code
    private Button registerButton;
    private String userType;
    private final String TEACHER_CODE = "TEACHER12";  // Example teacher code (8 alphanumeric characters)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newUsername = findViewById(R.id.newUsername);
        newPassword = findViewById(R.id.newPassword);
        teacherCodeEditText = findViewById(R.id.teacherCode);  // Initialize the teacher code field
        registerButton = findViewById(R.id.registerButton);

        // Get the userType ("student" or "teacher")
        Intent intent = getIntent();
        userType = intent.getStringExtra("userType");

        // If registering as a teacher, show the teacher code field
        if (userType.equals("teacher")) {
            teacherCodeEditText.setVisibility(View.VISIBLE);  // Show teacher code input
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsername.getText().toString();
                String password = newPassword.getText().toString();
                String teacherCode = teacherCodeEditText.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (userType.equals("teacher")) {
                        // Validate the teacher code for alphanumeric and 8 characters
                        if (!isValidTeacherCode(teacherCode)) {
                            Toast.makeText(RegisterActivity.this, "Invalid Teacher Code. Must be 8 alphanumeric characters.", Toast.LENGTH_SHORT).show();
                            return;  // Stop further processing if the code is wrong
                        }
                    }

                    String prefsName = userType.equals("student") ? "StudentPrefs" : "TeacherPrefs";

                    // Check if username already exists
                    if (isUsernameTaken(prefsName, username)) {
                        Toast.makeText(RegisterActivity.this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save user data in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Username", username);
                        editor.putString("Password", password);
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, userType + " registration successful", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to login page
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate the teacher code to ensure it's 8 alphanumeric characters
    private boolean isValidTeacherCode(String teacherCode) {
        return teacherCode.matches("^[a-zA-Z0-9]{8}$");
    }

    private boolean isUsernameTaken(String prefsName, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("Username", null);
        return storedUsername != null && storedUsername.equals(username);
    }
}
