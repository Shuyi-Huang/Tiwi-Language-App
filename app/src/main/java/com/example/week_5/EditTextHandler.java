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

public class EditTextHandler extends AppCompatActivity {

    private EditText searchEnglishWord;
    private EditText newEnglishWord;
    private EditText newTiwiWord;
    private Button searchButton;
    private Button updateButton;
    private ImageView backButton;  // Back button for navigation
    private JSONArray wordsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        searchEnglishWord = findViewById(R.id.searchEnglishWord);
        newEnglishWord = findViewById(R.id.newEnglishWord);
        newTiwiWord = findViewById(R.id.newTiwiWord);
        searchButton = findViewById(R.id.searchButton);
        updateButton = findViewById(R.id.updateButton);
        backButton = findViewById(R.id.back_button);  // Initialize the back button

        // Load JSON from storage
        try {
            String json = loadJSONFromStorage();
            if (json != null) {
                wordsArray = new JSONArray(json);
            } else {
                wordsArray = new JSONArray();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Search for the word
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchWord = searchEnglishWord.getText().toString();
                if (!searchWord.isEmpty()) {
                    searchAndUpdateWord(searchWord);
                } else {
                    Toast.makeText(EditTextHandler.this, "Please enter a word to search.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update the word
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEngWord = newEnglishWord.getText().toString();
                String newTiwiW = newTiwiWord.getText().toString();
                if (!newEngWord.isEmpty() && !newTiwiW.isEmpty()) {
                    updateWord(newEngWord, newTiwiW);
                } else {
                    Toast.makeText(EditTextHandler.this, "Please fill both fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button functionality to return to TeacherDashboard
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTextHandler.this, TeacherDashboard.class);
                startActivity(intent);
                finish();  // Close the current activity
            }
        });
    }

    private void searchAndUpdateWord(String searchWord) {
        try {
            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject word = wordsArray.getJSONObject(i);
                String english = word.getString("english");

                if (english.equalsIgnoreCase(searchWord)) {
                    newEnglishWord.setText(english);
                    newTiwiWord.setText(word.getString("tiwi"));
                    return;
                }
            }
            Toast.makeText(this, "Word not found", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateWord(String newEnglish, String newTiwi) {
        try {
            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject word = wordsArray.getJSONObject(i);
                String english = word.getString("english");

                if (english.equalsIgnoreCase(newEnglish)) {
                    word.put("tiwi", newTiwi); // Update Tiwi word
                    saveWordsToStorage();
                    Toast.makeText(this, "Word updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Word not found for update", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveWordsToStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("WordsData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("words_json", wordsArray.toString());
        editor.apply();
    }

    private String loadJSONFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("WordsData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("words_json", null);
    }
}
