package fr.azhot.mareu.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import fr.azhot.mareu.R;
import fr.azhot.mareu.events.MustRefreshAddButtonEvent;

public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        if (textView != null) {
            if (position != 0) { // set items looks
                textView.setTextAppearance(textView.getContext(), R.style.spinnerItemStyle);
            }
        }
        EventBus.getDefault().post(new MustRefreshAddButtonEvent());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
