package com.codinginflow.mvvm_room_example;

import android.app.Application;

public class MainApplication extends Application {

    private static Application sApplication;

    public MainApplication() {
        sApplication = this;
    }

    public static Application getInstance() {
        return sApplication;
    }
}
