package com.bornaapp.TodaysHadith;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {

    private static App instance = null;

    /**
     * ctor cannot be 'private' like other singletons,
     * because this ctor will be called by system. This
     * exposes the class to unauthorised instantiations.
     */
    public App() {
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}