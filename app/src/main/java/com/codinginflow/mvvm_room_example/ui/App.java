package com.codinginflow.mvvm_room_example.ui;

import android.app.Application;

public class App extends Application {

    private static Application application;

    public App() {
        application = this;
    }

    public static Application getInstance() {
        return application;
    }
}