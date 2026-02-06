package com.thinkspark.model;

import com.thinkspark.view.ManageNgosView; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {

    private static final ObservableList<Disaster> disasterReports = FXCollections.observableArrayList();

    private static final ObservableList<Alert> broadcastAlerts = FXCollections.observableArrayList();

    private static final ObservableList<Task> assignedTasks = FXCollections.observableArrayList();

    private static final ObservableList<Volunteer> volunteers = FXCollections.observableArrayList();

    private static final ObservableList<ManageNgosView.NgoItem> ngos = FXCollections.observableArrayList();

    static {
        ngos.addAll(
            new ManageNgosView.NgoItem("Sahayata Foundation", "Verified", "contact@sahayata.org"),
            new ManageNgosView.NgoItem("Hope India Initiative", "Verified", "info@hopeindia.org"),
            new ManageNgosView.NgoItem("Nashik Community Aid", "Pending Verification", "apply@nashikca.com")
        );

        broadcastAlerts.add(
            new Alert("Weather Warning", "Heavy rainfall expected in Nashik region for the next 48 hours. Please stay indoors.", "High")
        );
    }


    public static ObservableList<Disaster> getDisasterReports() {
        return disasterReports;
    }

    public static ObservableList<Alert> getBroadcastAlerts() {
        return broadcastAlerts;
    }

    public static ObservableList<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public static ObservableList<Volunteer> getVolunteers() {
        return volunteers;
    }

    public static ObservableList<ManageNgosView.NgoItem> getNgos() {
        return ngos;
    }
}