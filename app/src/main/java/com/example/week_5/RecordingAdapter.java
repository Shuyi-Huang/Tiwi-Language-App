package com.example.week_5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {

    private Context context;
    private List<Recording> recordingList;
    private List<Recording> selectedRecordings = new ArrayList<>(); // List to keep track of selected recordings
    private Consumer<Recording> playRecordingCallback;  // Callback for playing recording

    // Updated constructor to include playRecording callback
    public RecordingAdapter(Context context, List<Recording> recordingList, Consumer<Recording> playRecordingCallback) {
        this.context = context;
        this.recordingList = recordingList;
        this.playRecordingCallback = playRecordingCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recording_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recording recording = recordingList.get(position);
        holder.recordingName.setText(recording.getName());
        holder.recordingUrl.setText(recording.getUrl());

        // Handle CheckBox for multi-selection
        holder.selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedRecordings.add(recording);
            } else {
                selectedRecordings.remove(recording);
            }
        });

        // Handle Play button
        holder.playButton.setOnClickListener(v -> playRecordingCallback.accept(recording));
    }

    @Override
    public int getItemCount() {
        return recordingList.size();
    }

    // Return the list of selected recordings
    public List<Recording> getSelectedRecordings() {
        return selectedRecordings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recordingName, recordingUrl;
        CheckBox selectCheckBox;  // Add CheckBox to allow selecting recordings
        ImageView playButton;     // Add Play button (ImageView)

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recordingName = itemView.findViewById(R.id.recording_name);
            recordingUrl = itemView.findViewById(R.id.recording_url);
            selectCheckBox = itemView.findViewById(R.id.select_checkbox);  // Find the CheckBox
            playButton = itemView.findViewById(R.id.play_button);  // Find the Play button (ImageView)
        }
    }
}
