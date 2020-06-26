package fr.azhot.mareu.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import fr.azhot.mareu.R;

public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private Listener mCallback;

    public MyOnItemSelectedListener(Listener callback) {
        this.mCallback = callback;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        if (textView != null) {
            if (position != 0) { // set items looks
                textView.setTextAppearance(textView.getContext(), R.style.spinnerItemStyle);
            }
        }
        WeakReference<Listener> callBackWeakReference = new WeakReference<>(mCallback);
        callBackWeakReference.get().onSpinnerSetSelection();
    }

    public interface Listener {
        void onSpinnerSetSelection();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
