package com.example.week_5;

public class Recording {
    private String id;  // To store Firestore document ID
    private String name;
    private String url;
    private boolean isSelected;  // Field to track whether the recording is selected

    // Default constructor required for Firestore
    public Recording() {}

    public Recording(String name, String url) {
        this.name = name;
        this.url = url;
        this.isSelected = false;  // Initialize as not selected
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;  // Method to set the Firestore document ID
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public boolean isSelected() {
        return isSelected;  // Return selection state
    }

    public void setSelected(boolean selected) {
        isSelected = selected;  // Set selection state
    }

    @Override
    public String toString() {
        return name;  // Display the name when needed
    }
}
