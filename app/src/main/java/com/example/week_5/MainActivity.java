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

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button studentLoginButton;
    private Button teacherLoginButton;
    private Button studentRegisterButton;
    private Button teacherRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        studentLoginButton = findViewById(R.id.studentLoginButton);
        teacherLoginButton = findViewById(R.id.teacherLoginButton);
        studentRegisterButton = findViewById(R.id.studentRegisterButton);
        teacherRegisterButton = findViewById(R.id.teacherRegisterButton);

        // Student Login
        studentLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin("StudentPrefs", "student", username.getText().toString(), password.getText().toString());
            }
        });

        // Teacher Login
        teacherLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin("TeacherPrefs", "teacher", username.getText().toString(), password.getText().toString());
            }
        });

        // Navigate to Student Registration
        studentRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("userType", "student");
                startActivity(intent);
            }
        });

        // Navigate to Teacher Registration
        teacherRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("userType", "teacher");
                startActivity(intent);
            }
        });
    }

    private void handleLogin(String prefsName, String userType, String inputUsername, String inputPassword) {
        SharedPreferences sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("Username", null);
        String storedPassword = sharedPreferences.getString("Password", null);

        if (storedUsername != null && storedUsername.equals(inputUsername) && storedPassword != null && storedPassword.equals(inputPassword)) {
            Toast.makeText(MainActivity.this, userType + " login successful", Toast.LENGTH_SHORT).show();

            // Store the username in SharedPreferences for later use (e.g., in StudentDashboard or FreedomWall)
            SharedPreferences generalPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = generalPrefs.edit();
            editor.putString("userName", inputUsername);  // Store the logged-in username
            editor.apply();

            if (userType.equals("teacher")) {
                // Redirect to Teacher Dashboard
                Intent intent = new Intent(MainActivity.this, TeacherDashboard.class);
                startActivity(intent);
            } else if (userType.equals("student")) {
                // Redirect to Student Dashboard
                Intent intent = new Intent(MainActivity.this, StudentDashboard.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(MainActivity.this, "Login failed. Incorrect credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
