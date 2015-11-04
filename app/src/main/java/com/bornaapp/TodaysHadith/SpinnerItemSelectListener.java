package com.bornaapp.TodaysHadith;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerItemSelectListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView parent, View view, int pos,long id) {
        switch(pos){
            case 0:
                App.get().Set_UpdateRate(UpdateRate.Low);
                SharedPrefs.SavePrefItem(App.get().getString(R.string.txt_prefKey_Delay), App.get().getResources().getInteger(R.integer.int_UpdateFactor_Low));
                break;
            case 1:
                App.get().Set_UpdateRate(UpdateRate.Medium);
                SharedPrefs.SavePrefItem(App.get().getString(R.string.txt_prefKey_Delay), App.get().getResources().getInteger(R.integer.int_UpdateFactor_Med));
                break;
            case 2:
                App.get().Set_UpdateRate(UpdateRate.High);
                SharedPrefs.SavePrefItem(App.get().getString(R.string.txt_prefKey_Delay), App.get().getResources().getInteger(R.integer.int_UpdateFactor_Hi));
                break;
            default:
                App.get().Set_UpdateRate(UpdateRate.Low);
        }
    }

    @Override
    public void onNothingSelected(AdapterView arg0) {
        App.get().Set_UpdateRate(UpdateRate.Low);
    }
}