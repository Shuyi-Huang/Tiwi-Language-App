package com.example.week_5;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListeningExerciseActivity extends AppCompatActivity {

    private ProgressBar audioProgressBar;
    private ImageView backButton;
    private RecyclerView recordingsRecyclerView;
    private RecordingAdapter recordingAdapter;
    private List<Recording> recordingList;
    private MediaPlayer mediaPlayer;

    private FirebaseFirestore db;
    private CollectionReference recordingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_exercise);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        recordingsRef = db.collection("recordings");

        // Initialize UI components
        audioProgressBar = findViewById(R.id.audioProgressBar);
        backButton = findViewById(R.id.back_button);
        recordingsRecyclerView = findViewById(R.id.recordingsRecyclerView);

        // Initialize RecyclerView
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordingList = new ArrayList<>();
        recordingAdapter = new RecordingAdapter(ListeningExerciseActivity.this, recordingList, new Consumer<Recording>() {
            @Override
            public void accept(Recording recording) {
                playRecording(recording);
            }
        });
        recordingsRecyclerView.setAdapter(recordingAdapter);

        // Load recordings from Firebase Firestore
        loadRecordings();

        // Back button logic to navigate to StudentDashboard
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListeningExerciseActivity.this, StudentDashboard.class);
            startActivity(intent);
            finish();  // Close this activity
        });
    }

    // Method to load recordings from Firebase Firestore
    private void loadRecordings() {
        recordingsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Recording recording = document.toObject(Recording.class);
                    recording.setId(document.getId());  // Set the Firestore document ID
                    recordingList.add(recording);
                }
                recordingAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ListeningExerciseActivity.this, "Error loading recordings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to play a selected recording
    private void playRecording(Recording recording) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(recording.getUrl()));
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing: " + recording.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing recording", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
