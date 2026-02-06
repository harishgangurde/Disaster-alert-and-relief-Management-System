package com.thinkspark.model;

/**
 * Represents a volunteer in the system.
 * This class is structured as a JavaBean for Firestore compatibility.
 */
public class Volunteer {

    private String name;
    private String email;
    private String role;

    public Volunteer() {}

   
    public Volunteer(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name;
    }
}