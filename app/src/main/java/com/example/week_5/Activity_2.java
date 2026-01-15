package com.example.week_5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.io.InputStream;
import java.util.List;
import java.util.Random;


public class Activity_2 extends AppCompatActivity {

        private TextView engWordTextView;
        private TextView dharugWordTextView;
        private List<String[]> data;
        private Random random = new Random();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_2);

            // Initialize TextViews
            engWordTextView = findViewById(R.id.EngWord);
            dharugWordTextView = findViewById(R.id.DrarugWord);

            // Initialize Button
            Button nextButton = findViewById(R.id.NextButton);

            try {
                // Load the words from excel
                InputStream inputStream = getAssets().open("DharugList.xlsx");
                DharugListReader dharugListReader = new DharugListReader();
                data = dharugListReader.readWordsFromExcel(inputStream);


                if (!data.isEmpty()) {
                    displayWords(data.get(0));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set set listener for button
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data != null && !data.isEmpty()) {
                        // randomize words
                        int index = random.nextInt(data.size());
                        displayWords(data.get(index));
                    }
                }
            });
        }

        private void displayWords(String[] words) {
            if (words.length > 0) {
                engWordTextView.setText(words[0]);
            }
            if (words.length > 1) {
                dharugWordTextView.setText(words[1]);
            }
        }
    }
