//package com.example.week_5;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SuccessPageActivity extends AppCompatActivity {
//
//    private Button continueButton, backButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_success_page);
//
//        // Bind buttons
//        continueButton = findViewById(R.id.continueButton);
//        backButton = findViewById(R.id.backButton);
//
//        // Set click listener for Continue button
//        continueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Continue to the next activity (you can link it to any activity)
//                Intent intent = new Intent(SuccessPageActivity.this, NextActivity.class); // Change NextActivity to your actual next activity
//                startActivity(intent);
//            }
//        });
//
//        // Set click listener for Back button
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go back to the previous activity (MatchWordsActivity)
//                finish(); // This will return to the previous activity
//            }
//        });
//    }
//}
package com.example.week_5;

import android.content.Intent; // Import this for resolving the Intent issue
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SuccessPageActivity extends AppCompatActivity {

    private Button continueButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);

        // Get message from intent
        String message = getIntent().getStringExtra("message");
        TextView successMessage = findViewById(R.id.successMessage);
        successMessage.setText(message);

        continueButton = findViewById(R.id.continueButton);
        backButton = findViewById(R.id.backButton);

        // Set listeners for buttons
        continueButton.setOnClickListener(v -> {
            // Action when clicking continue, you can define where to go next
            Intent intent = new Intent(SuccessPageActivity.this, MatchWordActivity2.class); // Change NextActivity to your next page
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            // Action when clicking back
            Intent intent = new Intent(SuccessPageActivity.this, MatchWordsActivity.class); // Change NextActivity to your next page
            startActivity(intent);
        });
    }
}
