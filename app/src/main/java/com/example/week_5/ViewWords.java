package com.example.week_5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewWords extends AppCompatActivity {

    private ListView wordsListView;
    private List<HashMap<String, String>> wordsList = new ArrayList<>();
    private ImageView backButton;  // Declare the back button
    private String source;  // Store the source (student or teacher)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_words);

        wordsListView = findViewById(R.id.wordsListView);
        backButton = findViewById(R.id.back_button);  // Initialize the back button

        // Get the source from the Intent extras
        source = getIntent().getStringExtra("source");

        // Set onClickListener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent;
            if ("teacher".equals(source)) {
                intent = new Intent(ViewWords.this, TeacherDashboard.class);
            } else {
                intent = new Intent(ViewWords.this, StudentDashboard.class);
            }
            startActivity(intent);
            finish();  // Close this activity
        });

        try {
            // Load the Excel file from assets
            InputStream inputStream = getAssets().open("TiwiList.xlsx");
            DharugListReader reader = new DharugListReader();
            List<String[]> wordsFromExcel = reader.readWordsFromExcel(inputStream);

            for (String[] word : wordsFromExcel) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Word", word[0]);
                map.put("Translation", word[1]);
                wordsList.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    wordsList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"Word", "Translation"},
                    new int[]{android.R.id.text1, android.R.id.text2});

            wordsListView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewWords.this, "Error loading Excel file", Toast.LENGTH_SHORT).show();
        }
    }
}
