package com.example.app.jpegstreamming;

import android.app.Application;

public class CheckingRunningApp extends Application {

    private static boolean activityVisible = true;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

}
