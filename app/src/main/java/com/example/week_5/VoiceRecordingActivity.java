package com.example.week_5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VoiceRecordingActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private EditText recordingNameEditText;
    private ProgressBar progressBar;
    private RecyclerView recordingsRecyclerView;
    private Button recordButton, stopButton, saveButton, deleteButton;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;

    private FirebaseFirestore db;
    private CollectionReference recordingsRef;
    private List<Recording> recordingList;
    private RecordingAdapter recordingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recording);

        // Request permissions for audio recording
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        recordingsRef = db.collection("recordings");

        // Initialize views
        recordingNameEditText = findViewById(R.id.recording_name_edit_text);
        progressBar = findViewById(R.id.progress_bar);
        recordingsRecyclerView = findViewById(R.id.recordings_recycler_view);
        recordButton = findViewById(R.id.record_button);
        stopButton = findViewById(R.id.stop_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        // Initialize RecyclerView
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize recording list and adapter
        recordingList = new ArrayList<>();
        recordingAdapter = new RecordingAdapter(this, recordingList, this::playRecording);  // Pass playRecording to adapter
        recordingsRecyclerView.setAdapter(recordingAdapter);

        // Load recordings from Firestore
        loadRecordings();

        // Set up button listeners
        recordButton.setOnClickListener(v -> startRecording());
        stopButton.setOnClickListener(v -> stopRecording());
        saveButton.setOnClickListener(v -> saveRecording());
        deleteButton.setOnClickListener(v -> deleteSelectedRecordings());

        // Handle back button functionality
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(VoiceRecordingActivity.this, TeacherDashboard.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void loadRecordings() {
        // Clear current recordings and load from Firestore
        recordingList.clear();
        recordingsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().forEach(document -> {
                    Recording recording = document.toObject(Recording.class);
                    recording.setId(document.getId());  // Set Firestore document ID
                    recordingList.add(recording);
                });
                recordingAdapter.notifyDataSetChanged();  // Notify adapter to update the list
            }
        });
    }

    // Start recording audio
    private void startRecording() {
        if (mediaRecorder == null) {
            audioFilePath = getExternalCacheDir().getAbsolutePath() + "/recording.3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Stop recording audio
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
        }
    }

    // Save the recorded audio
    private void saveRecording() {
        String recordingName = recordingNameEditText.getText().toString();
        if (!recordingName.isEmpty() && audioFilePath != null) {
            Uri audioUri = Uri.fromFile(new File(audioFilePath));
            Recording newRecording = new Recording(recordingName, audioUri.toString());

            // Save recording to Firestore
            recordingsRef.add(newRecording)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(VoiceRecordingActivity.this, "Recording saved!", Toast.LENGTH_SHORT).show();
                        recordingNameEditText.setText("");
                        loadRecordings();  // Refresh the list
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(VoiceRecordingActivity.this, "Failed to save recording", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please enter a name for the recording", Toast.LENGTH_SHORT).show();
        }
    }

    // Play the selected recording
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
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing recording", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete the selected recordings
    private void deleteSelectedRecordings() {
        List<Recording> selectedRecordings = recordingAdapter.getSelectedRecordings();

        if (selectedRecordings.isEmpty()) {
            Toast.makeText(this, "No recordings selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete selected recordings from Firestore
        for (Recording recording : selectedRecordings) {
            db.collection("recordings").document(recording.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Deleted: " + recording.getName(), Toast.LENGTH_SHORT).show();
                        recordingList.remove(recording);  // Remove from list
                        recordingAdapter.notifyDataSetChanged();  // Update RecyclerView
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete " + recording.getName(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }
}
