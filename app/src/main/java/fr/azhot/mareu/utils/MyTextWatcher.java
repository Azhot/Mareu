package fr.azhot.mareu.utils;

import android.text.Editable;
import android.text.TextWatcher;

import java.lang.ref.WeakReference;

public class MyTextWatcher implements TextWatcher {

    private Listener mCallback;

    public MyTextWatcher(Listener callback) {
        this.mCallback = callback;
    }

    @Override
    public void afterTextChanged(Editable s) {
        WeakReference<MyTextWatcher.Listener> callbackWeakReference = new WeakReference<>(mCallback);
        callbackWeakReference.get().onTextChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public interface Listener {
        void onTextChanged();
    }
}
