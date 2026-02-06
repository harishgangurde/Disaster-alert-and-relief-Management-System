package com.thinkspark.model;


import java.util.Date;

public class Alert {

    private String title;
    private String message;
    private String priority;

    @ServerTimestamp
    private Date timestamp;

    public Alert() {}

    public Alert(String title, String message, String priority) {
        this.title = title;
        this.message = message;
        this.priority = priority;
    }


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Alert [" + priority + "]: " + title;
    }
}
