package com.thinkspark;

import com.thinkspark.view.StartupScreen;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(StartupScreen.class,args);
    }
} 