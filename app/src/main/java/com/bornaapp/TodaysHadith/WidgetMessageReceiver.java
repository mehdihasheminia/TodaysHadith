package com.bornaapp.TodaysHadith;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetMessageReceiver extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {

        // processing received intents
        //
        if (intent.getAction().equals("com.bornaapp.appwidget.action.APPWIDGET_PAUSE")) {
            App.get().alarm.RequestStop();
            App.get().alarm.Set();
        } else if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            App.get().alarm.RequestStart();
        }
        //running onUpdate(), onDisabled() & ... operations
        //
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //1st time initialization
        if (App.get().needsInit()) {
            App.get().Init();
            App.get().UpdateASAP();
        }

        //Update Alarm state
        App.get().alarm.Set();

        if (App.get().TimeToUpdate()) {
            App.get().randomString.Next();
            UpdateText(context, appWidgetManager, appWidgetIds);
        } else if (App.get().ShouldForceUpdate()) {
            UpdateText(context, appWidgetManager, appWidgetIds);
        } else if (App.get().WidgetCountChanged(appWidgetIds.length)) {
            UpdateText(context, appWidgetManager, appWidgetIds);
        } else {
            UpdateControl(context, appWidgetManager, appWidgetIds);
        }
        App.get().PrepareForNextUpdate();
    }

    private void UpdateText(Context _context, AppWidgetManager _appWidgetManager, int[] _appWidgetIds) {
        //query each appWidget
        for (int widgetId : _appWidgetIds) {
            //access remote view
            RemoteViews views = new RemoteViews(_context.getPackageName(), R.layout.widgetlayout);
            Intent intent = new Intent(_context, Configure.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.tTxtView, pendingIntent);
            views.setOnClickPendingIntent(R.id.hTxtView, pendingIntent);
            //update Text fields
            String[] separated = App.get().randomString.Current().split("::");
            views.setTextViewText(R.id.hTxtView, separated[0]);
            views.setTextViewText(R.id.tTxtView, separated[1]);
            App.get().UpdateDone();
            //Update AppWidget
            _appWidgetManager.updateAppWidget(widgetId, views);
        }
        App.get().CancelForceupdate();
    }

    private void UpdateControl(Context _context, AppWidgetManager _appWidgetManager, int[] _appWidgetIds) {
        //query each appWidget
        for (int currentWidgetId : _appWidgetIds) {
            //access remote view
            RemoteViews views = new RemoteViews(_context.getPackageName(), R.layout.widgetlayout);
            Intent intent = new Intent(_context, Configure.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, currentWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.tTxtView, pendingIntent);
            views.setOnClickPendingIntent(R.id.hTxtView, pendingIntent);
            //Update AppWidget
            _appWidgetManager.updateAppWidget(currentWidgetId, views);
        }
    }

    @Override
    public void onDisabled(Context _context) {
        App.get().alarm.RequestStop();
        App.get().alarm.Set();
        App.get().stopService();
        super.onDisabled(_context);
    }

    @Override
    public void onDeleted(Context _context, int[] _appWidgetIds) {
        App.get().WidgetCountChanged(_appWidgetIds.length);
        super.onDeleted(_context, _appWidgetIds);
    }
}
