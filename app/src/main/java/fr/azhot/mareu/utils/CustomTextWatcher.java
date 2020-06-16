package fr.azhot.mareu.utils;

import android.text.Editable;
import android.text.TextWatcher;

import static fr.azhot.mareu.ui.meeting_add.AddMeetingActivity.refreshAddButton;

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
        refreshAddButton();
    }
}
