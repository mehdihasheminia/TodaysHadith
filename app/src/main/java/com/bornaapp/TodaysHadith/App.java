package com.bornaapp.TodaysHadith;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class App extends Application {

    private static App instance = null;

    private App() {
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
    //////////////////-------------old------------------------------

    public static App get(){
    return instance;
    }

    private int widgetCount = 0;

    public boolean WidgetCountChanged(int newWidCount) {
        boolean change = (widgetCount != newWidCount);
        if (change)
            widgetCount = newWidCount;
        return (change);
    }
}