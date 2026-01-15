package com.example.week_5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddText extends AppCompatActivity {

    private EditText englishWordEditText;
    private EditText tiwiWordEditText;
    private Button addButton;
    private ImageView backButton;  // Declare the back button
    private JSONArray wordsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        englishWordEditText = findViewById(R.id.englishWordEditText);
        tiwiWordEditText = findViewById(R.id.tiwiWordEditText);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.back_button);  // Initialize the back button

        // Load existing words from storage
        String json = loadJSONFromStorage();
        try {
            wordsArray = (json != null) ? new JSONArray(json) : new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            wordsArray = new JSONArray();
        }

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord();
            }
        });

        // Back button click listener to return to TeacherDashboard
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddText.this, TeacherDashboard.class);
                startActivity(intent);
                finish();  // Close the current activity
            }
        });
    }

    private void addWord() {
        String englishWord = englishWordEditText.getText().toString().trim();
        String tiwiWord = tiwiWordEditText.getText().toString().trim();

        if (englishWord.isEmpty() || tiwiWord.isEmpty()) {
            Toast.makeText(this, "Both fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate English words
        for (int i = 0; i < wordsArray.length(); i++) {
            try {
                JSONObject word = wordsArray.getJSONObject(i);
                if (word.getString("english").equalsIgnoreCase(englishWord)) {
                    Toast.makeText(this, "This English word already exists!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Add the new word
        JSONObject newWord = new JSONObject();
        try {
            newWord.put("english", englishWord);
            newWord.put("tiwi", tiwiWord);
            wordsArray.put(newWord);

            // Save the updated JSON to storage
            saveJsonToStorage(wordsArray.toString());
            Toast.makeText(this, "Word added successfully!", Toast.LENGTH_SHORT).show();
            englishWordEditText.setText("");
            tiwiWordEditText.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromStorage() {
        // Load the existing words from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("WordsData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("words_json", null);
    }

    private void saveJsonToStorage(String json) {
        // Save the updated JSON back to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("WordsData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("words_json", json);
        editor.apply();
    }
}
