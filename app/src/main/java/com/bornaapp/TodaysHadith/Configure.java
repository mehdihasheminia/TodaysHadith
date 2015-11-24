package com.bornaapp.TodaysHadith;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class Configure extends Activity {

    //region Activity methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find the widget id from the intent.
        // And stops activity from running if there is no valid calling widget
        int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // Set our layout for the Configure Activity
        setContentView(R.layout.configurelayout);

        //Update configuration UI
        UpdateText(App.get().randomString.Current());

        // Bind the action for the buttons.
        findViewById(R.id.nextBtn).setOnClickListener(mOnClickNextBtn);
        findViewById(R.id.PreviousBtn).setOnClickListener(mOnClickPrevBtn);
        findViewById(R.id.copyBtn).setOnClickListener(mOnClickCopyBtn);
        findViewById(R.id.shareBtn).setOnClickListener(mOnClickShareBtn);
        findViewById(R.id.supportBtn).setOnClickListener(mOnClickSupportBtn);
        findViewById(R.id.suggestBtn).setOnClickListener(mOnClickSuggestBtn);

        // Bind the action for the Spinner.
        RepositionSpinner();
        Spinner tmpSpinner = (Spinner) findViewById(R.id.spinner);
        tmpSpinner.setOnItemSelectedListener(new SpinnerItemSelectListener());

        //deactivate appWidget
        BroadcastPauseWidget();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        BroadcastResumeWidget();
        super.onStop();
    }
    //endregion

    //region Broadcasting methods
     private void BroadcastPauseWidget() {
         WidgetMessageSender messageSender = new WidgetMessageSender(this);
         messageSender.Broadcast("com.bornaapp.appwidget.action.APPWIDGET_PAUSE");
    }

    private void BroadcastResumeWidget() {
        WidgetMessageSender messageSender = new WidgetMessageSender(this);
        messageSender.Broadcast("android.appwidget.action.APPWIDGET_UPDATE");
    }
    //endregion

    //region User Interface
    private void UpdateText(String txt) {
        String[] separated = txt.split("::");

        if (separated.length > 1)
            txt = separated[0] + " " + separated[1];

        TextView tmpTxtView = (TextView) findViewById(R.id.quoteTxtView2);
        tmpTxtView.setText(txt);
    }

    View.OnClickListener mOnClickNextBtn = new View.OnClickListener() {
        public void onClick(View v) {
            UpdateText(App.get().randomString.Next());
            App.get().Forceupdate();
        }
    };

    View.OnClickListener mOnClickPrevBtn = new View.OnClickListener() {
        public void onClick(View v) {
            UpdateText(App.get().randomString.Previous());
            App.get().Forceupdate();
        }
    };

    View.OnClickListener mOnClickShareBtn = new View.OnClickListener() {
        public void onClick(View v) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            String shareBody = App.get().randomString.Current();
            String[] separated = shareBody.split("::");
            if (separated.length > 1)
                shareBody = separated[0] + " " + separated[1];

            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    };

    View.OnClickListener mOnClickCopyBtn = new View.OnClickListener() {
        public void onClick(View v) {

            //Copy text to clipboard
            String txt = App.get().randomString.Current();
            String[] separated = txt.split("::");
            if (separated.length > 1)
                txt = separated[0] + " " + separated[1];

            if (txt.length() > 0) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboardMgr = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardMgr.setText(txt);
                } else {
                    // this api requires SDK version 11 and above, so suppress warning for now
                    android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied text", txt);
                    clipboardMgr.setPrimaryClip(clip);
                }
            }

            //Let user know
            Context context = Configure.this;
            Toast.makeText(context, context.getString(R.string.txt_copy_message), Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener mOnClickSupportBtn = new View.OnClickListener() {
        public void onClick(View v) {
            Context context = Configure.this;
            //Prepare Email
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            String toList[] = {context.getString(R.string.txt_email_Address)};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, toList);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.txt_email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.txt_email_message));
            //Send Email
            try {
                startActivity(Intent.createChooser(emailIntent, context.getString(R.string.txt_email_chooser)));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, context.getString(R.string.txt_email_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener mOnClickSuggestBtn = new View.OnClickListener() {
        public void onClick(View v) {
            String shareBody = App.get().getResources().getString(R.string.txt_Suggest_Message);

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    };

    void RepositionSpinner()
    {
        int pos = 0;
        if(App.get().Get_UpdateRate() == App.get().getResources().getInteger(R.integer.int_UpdateFactor_Low))
            pos = 0;
        else if(App.get().Get_UpdateRate() == App.get().getResources().getInteger(R.integer.int_UpdateFactor_Med))
            pos = 1;
        else if(App.get().Get_UpdateRate() == App.get().getResources().getInteger(R.integer.int_UpdateFactor_Hi))
            pos = 2;

        Spinner tmpSpinner = (Spinner) findViewById(R.id.spinner);
        tmpSpinner.setSelection(pos);
    }

    //endregion
}