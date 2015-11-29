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
        if (intent.getAction().equals("com.bornaapp.appwidget.action.APPWIDGET_PAUSE")) {
            Timer.pause();
        } else if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            Timer.run();
        }

        //running onUpdate(), onDisabled() & ... operations
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        if (Timer.getElapsedMinutes() >= getWidgetUpdateRateFromPrefs()) {
            Timer.reset();
            RandomString.next();
        }
        UpdateWidget(context, appWidgetManager, appWidgetIds);
        Timer.Update();
    }

    @Override
    public void onEnabled(Context _context) {
        //1st time initialization
        if (!RandomString.isInitialized())
            RandomString.init();
        if (!Timer.isInitialized())
            Timer.init();
    }

    @Override
    public void onDisabled(Context _context) {
        Timer.pause();
        Timer.stop();
        super.onDisabled(_context);
    }

    @Override
    public void onDeleted(Context _context, int[] _appWidgetIds) {
//        App.get().WidgetCountChanged(_appWidgetIds.length);
        super.onDeleted(_context, _appWidgetIds);
    }

    //private methods
    private int getWidgetUpdateRateFromPrefs() {
        Context context = App.getContext();
        int widgetUpdateRate;
        try {
            widgetUpdateRate = SharedPrefs.LoadPref_Int(context, context.getString(R.string.txt_prefKey_Delay));
        } catch (Exception e) {
            widgetUpdateRate = -1;
        }
        if (widgetUpdateRate < 0) {
            widgetUpdateRate = context.getResources().getInteger(R.integer.int_UpdateFactor_Low);
        }
        return widgetUpdateRate;
    }

    private void UpdateWidget(Context _context, AppWidgetManager _appWidgetManager, int[] _appWidgetIds) {
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
            String[] separated = RandomString.current().split("::");
            views.setTextViewText(R.id.hTxtView, separated[0]);
            views.setTextViewText(R.id.tTxtView, separated[1]);
            //Update AppWidget
            _appWidgetManager.updateAppWidget(widgetId, views);
        }
    }
}
