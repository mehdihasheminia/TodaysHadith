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

    //////////////////

    public static App get(){
    return instance;
    }

    private int updateCounter;
    private int widgetUpdateRate;
    private boolean forceUpdate = false;

    private int widgetCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        // Singleton private instantiation
        new App();
    }

    public boolean needsInit() {
        return false;
    }

    public void Init() {

        try {
            widgetUpdateRate = SharedPrefs.LoadPref_Int(getApplicationContext(),this.getString(R.string.txt_prefKey_Delay));
        } catch (Exception e) {
            widgetUpdateRate = -1;
        }
        if (widgetUpdateRate < 0) {
            widgetUpdateRate = this.getResources().getInteger(R.integer.int_UpdateFactor_Low);
        }
    }

    public void UpdateASAP() {
        updateCounter = widgetUpdateRate;
    }

    public void UpdateDone() {
        updateCounter = 0;
    }

    public void PrepareForNextUpdate() {
        updateCounter++;
    }

    public boolean TimeToUpdate() {
        return (updateCounter >= widgetUpdateRate);
    }

    public void Set_UpdateRate(UpdateRate rate) {
        switch (rate) {
            case High:
                widgetUpdateRate = this.getResources().getInteger(R.integer.int_UpdateFactor_Hi);
                break;
            case Medium:
                widgetUpdateRate = this.getResources().getInteger(R.integer.int_UpdateFactor_Med);
                break;
            case Low:
                widgetUpdateRate = this.getResources().getInteger(R.integer.int_UpdateFactor_Low);
                break;
            default:
                widgetUpdateRate = this.getResources().getInteger(R.integer.int_UpdateFactor_Low);
        }
    }

    public int Get_UpdateRate() {
        return widgetUpdateRate;
    }

    public void Forceupdate() {
        forceUpdate = true;
    }

    public void CancelForceupdate() {
        forceUpdate = false;
    }

    public boolean ShouldForceUpdate() {
        return forceUpdate;
    }

    public boolean WidgetCountChanged(int newWidCount) {
        boolean change = (widgetCount != newWidCount);
        if (change)
            widgetCount = newWidCount;
        return (change);
    }

    public void stopService(){
        instance.stopService(new Intent(instance, UpdateService.class));
    }
}