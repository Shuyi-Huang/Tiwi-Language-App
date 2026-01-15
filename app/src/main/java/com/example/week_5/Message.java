package com.example.week_5;

public class Message {
    private String id;  // Firestore document ID
    private String name;
    private String message;

    // Default constructor required for Firestore
    public Message() {}

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
