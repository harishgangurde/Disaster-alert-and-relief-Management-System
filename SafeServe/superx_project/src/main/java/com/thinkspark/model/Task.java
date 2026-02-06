package com.thinkspark.model;

public class Task {

    private String description;
    private String assignedTo; 
    private String status;     
    private String disasterId; 
    private String firebaseDocId; 

    public Task() {} 

  
    public Task(String description, String assignedTo, String disasterId) {
        this.description = description;
        this.assignedTo = assignedTo;
        this.disasterId = disasterId;
        this.status = "Assigned"; 
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDisasterId() { return disasterId; }
    public void setDisasterId(String disasterId) { this.disasterId = disasterId; }

    public String getFirebaseDocId() { return firebaseDocId; }
    public void setFirebaseDocId(String firebaseDocId) { this.firebaseDocId = firebaseDocId; }
}