package com.bornaapp.TodaysHadith;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {

    //region Service method
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        WidgetMessageSender messageSender = new WidgetMessageSender(getApplicationContext());
        messageSender.Broadcast("android.appwidget.action.APPWIDGET_UPDATE");

        //int passedID = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,-1);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //endregion
}
