package ua.tinder.model;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int senderId;
    private int recipientId;
    private String content;
    private Timestamp createdAt;

    public Message(int id, int senderId, int recipientId, String content, Timestamp createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getRecipientId() { return recipientId; }
    public String getContent() { return content; }
    public Timestamp getCreatedAt() { return createdAt; }
}