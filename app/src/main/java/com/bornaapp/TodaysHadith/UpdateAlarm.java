package com.bornaapp.TodaysHadith;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;

public class UpdateAlarm {

    private static UpdateAlarm instance = null;

    //ctor
    private UpdateAlarm() {
        instance = this;
    }

    //private methods


    //public methods
    public static boolean isInitialized() {
        return (instance != null);
    }

    public static void init() {
        // Singleton private instantiation
        new UpdateAlarm();
    }

    private boolean isAlarmStarted = false;
    private boolean alarmSetRequest = false;

    public void Set() {

        AlarmManager alarms = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);

        if (alarmSetRequest) {
            if (isAlarmStarted)
                return;
            int interval = App.getContext().getResources().getInteger(R.integer.int_Alarm_Interval);
            // As we just want to have one alarm in app,
            // we do not send appWidgetId for each appWidget instance
            PendingIntent pending = makeControlPendingIntent("update", 1);
            alarms.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), interval, pending);

            isAlarmStarted = true;
        } else {
            if (!isAlarmStarted)
                return;
            //As we just want to have one alarm in app,
            // we do not send appWidgetId for each appWidget instance
            PendingIntent pending = makeControlPendingIntent("update", 1);
            alarms.cancel(pending);

            isAlarmStarted = false;
        }
    }

    private PendingIntent makeControlPendingIntent(String command, int appWidgetId) {
        Intent intent = new Intent(App.getContext(), UpdateService.class);
        intent.setAction(command);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // The Uri data is to make the PendingIntent unique
        Uri uri = Uri.withAppendedPath(Uri.parse("bornaapAppWidget://widget/id/#" + command + appWidgetId),
                String.valueOf(appWidgetId));
        intent.setData(uri);
        return (PendingIntent.getService(App.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void RequestStart() {
        alarmSetRequest = true;
    }

    public void RequestStop() {
        alarmSetRequest = false;
    }
}
