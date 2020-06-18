package fr.azhot.mareu.utils;

import android.text.Editable;
import android.text.TextWatcher;

import org.greenrobot.eventbus.EventBus;

import fr.azhot.mareu.events.MustRefreshAddButtonEvent;

public class CustomTextWatcher implements TextWatcher {

    public CustomTextWatcher() {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        EventBus.getDefault().post(new MustRefreshAddButtonEvent());
    }
}
