package com.example.stylespo.model;


public class FriendReq{
    private String senderId;
    private String receiverId;
    private String status;

    // Required public no-argument constructor
    public FriendReq() {
    }

    public FriendReq(String senderId, String receiverId, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }
    public String getSender() {
        return senderId;
    }

    public void setSender(String sender) {
        this.senderId = sender;
    }

    public String getReceiver() {
        return receiverId;
    }

    public void setReceiver(String receiver) {
        this.receiverId = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}