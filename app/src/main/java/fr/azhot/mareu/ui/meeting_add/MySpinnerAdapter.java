package fr.azhot.mareu.ui.meeting_add;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.azhot.mareu.R;

public class MySpinnerAdapter extends ArrayAdapter<String> {

    List<String> mStrings;

    public MySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> strings) {
        super(context, resource, strings);
        this.mStrings = strings;
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public List<String> getList() {
        return mStrings;
    }

    public void setList(List<String> objects) {
        this.mStrings.clear();
        this.mStrings.addAll(objects);
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0; // disable hint on dropdown menu
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (position == 0) { // set hint looks on dropdown menu
            textView.setTextAppearance(textView.getContext(), R.style.spinnerHintItemStyle);
        } else {
            textView.setTextAppearance(textView.getContext(), R.style.spinnerDropDownItemStyle);
        }
        return view;
    }

}
