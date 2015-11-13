package com.bornaapp.TodaysHadith;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ClockService extends Service {

    //region Service method
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //new things
        int i = 0;
        i++;

        BroadcastUpdateWidget();
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

    //region Broadcasting methods
    private void BroadcastUpdateWidget() {

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
        Intent updateIntent = new Intent(context, WidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(updateIntent);
    }
    //endregion
}
