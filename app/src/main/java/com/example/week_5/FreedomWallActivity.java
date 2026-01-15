package com.example.week_5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class FreedomWallActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button submitButton;
    private ListView messageListView;
    private ArrayList<Message> messageList;  // List of Message objects
    private MessageAdapter messageAdapter;

    // Firestore instance
    private FirebaseFirestore db;
    private CollectionReference messagesRef;
    private String userName;  // Store the user's name here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freedom_wall);

        // Retrieve the user's name passed from the StudentDashboard
        userName = getIntent().getStringExtra("userName");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        messagesRef = db.collection("messages");

        // Initialize views
        messageInput = findViewById(R.id.messageInput);
        submitButton = findViewById(R.id.submitButton);
        messageListView = findViewById(R.id.messageListView);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        // Retrieve messages in real-time from Firestore
        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FreedomWallActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }

                messageList.clear();  // Clear the list to avoid duplication
                for (QueryDocumentSnapshot doc : value) {
                    Message message = doc.toObject(Message.class);
                    message.setId(doc.getId());  // Set the document ID to the message
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();  // Notify adapter that data has changed
            }
        });

        // Set onClickListener for Submit Button
        submitButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();

            if (!messageText.isEmpty()) {
                // Create a map to store the message with the user's name
                Map<String, Object> newMessage = new HashMap<>();
                newMessage.put("name", userName);  // Use the user's name passed from the previous activity
                newMessage.put("message", messageText);

                // Add the message to Firestore
                messagesRef.add(newMessage)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(FreedomWallActivity.this, "Message submitted!", Toast.LENGTH_SHORT).show();

                            // Clear input fields after submission
                            messageInput.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(FreedomWallActivity.this, "Failed to submit message", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(FreedomWallActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button functionality to return to StudentDashboard
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(FreedomWallActivity.this, StudentDashboard.class);
            startActivity(intent);
            finish();
        });
    }
}
