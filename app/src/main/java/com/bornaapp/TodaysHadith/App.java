package com.bornaapp.TodaysHadith;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {

    private static App instance = null;

    public App() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Singleton private instantiation
        new App();
    }

    public static void Toast(String message) {
        Toast.makeText(instance, message, Toast.LENGTH_SHORT).show();
    }
    //////////////////-------------old------------------------------

    private int widgetCount = 0;

    public boolean WidgetCountChanged(int newWidCount) {
        boolean change = (widgetCount != newWidCount);
        if (change)
            widgetCount = newWidCount;
        return (change);
    }
}