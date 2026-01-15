package com.example.week_5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private List<Message> messages;
    private FirebaseFirestore db;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
        db = FirebaseFirestore.getInstance();  // Firestore instance for deletion
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_message_item, parent, false);
        }

        // Get current message
        Message message = getItem(position);

        // Initialize views
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // Set the message text
        messageTextView.setText(message.getName() + ": " + message.getMessage());

        // Handle delete button click
        deleteButton.setOnClickListener(v -> {
            // Remove the message from Firestore
            db.collection("messages").document(message.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the message from the list and notify the adapter
                        messages.remove(position);
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure if the message could not be deleted
                    });
        });

        return convertView;
    }
}
