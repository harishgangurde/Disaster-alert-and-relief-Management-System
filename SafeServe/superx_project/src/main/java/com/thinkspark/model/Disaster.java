package com.thinkspark.model;

import com.google.cloud.firestore.annotation.ServerTimestamp;
import java.util.Date;

public class Disaster {

    private String type;
    private String location;
    private String description;
    private String citizenName;
    private String imageUrl;
    private String status;
    private String firebaseDocId; 

    @ServerTimestamp
    private Date timestamp;

    public Disaster() {}
    
    public Disaster(String type, String location, String description, String citizenName) {
        this.type = type;
        this.location = location;
        this.description = description;
        this.citizenName = citizenName;
        this.status = "Pending"; 
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; } 

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFirebaseDocId() { return firebaseDocId; }
    public void setFirebaseDocId(String firebaseDocId) { this.firebaseDocId = firebaseDocId; }

    @Override
    public String toString() {
        return "Disaster Report [Type='" + type + "', Location='" + location + "', Status='" + status + "']";
    }
}