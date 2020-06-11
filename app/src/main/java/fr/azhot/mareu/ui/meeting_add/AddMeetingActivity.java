package fr.azhot.mareu.ui.meeting_add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.azhot.mareu.R;
import fr.azhot.mareu.databinding.ActivityAddMeetingBinding;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;

public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String NEW_MEETING_EXTRA = "new_meeting";
    private ActivityAddMeetingBinding mBinding;
    private Calendar mStartTime;
    private Calendar mEndTime;

    // TODO : recyclerview for participants + regex for checking email (check native android function ?) + unit test
    // TODO : on submit check length of editTexts
    // set booleans to check whether to enable the add meeting button
    private boolean mSubjectFilledIn;
    private boolean mParticipantsFilledIn;
    private boolean mRoomFilledIn;
    private boolean mPriorityFilledIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartTime = Calendar.getInstance();
        initUI();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mStartTime.set(year, month, dayOfMonth);
        mBinding.addMeetingActivityDatePickerStartButton.setText(getDateToString(mStartTime));
        refreshEndTimeDisplay(mStartTime);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mStartTime.set(Calendar.MINUTE, minute);
        mStartTime.set(Calendar.SECOND, 0);
        mStartTime.set(Calendar.MILLISECOND, 0);
        mBinding.addMeetingActivityTimePickerStartButton.setText(getTimeToString(mStartTime));
        refreshEndTimeDisplay(mStartTime);
    }

    @Override
    public void onBackPressed() { // override onBackPressed to set-up an AlertDialog if user tries to back without saving
        if (mSubjectFilledIn || mParticipantsFilledIn || mRoomFilledIn || mPriorityFilledIn) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.add_meeting_activity_discard_meeting)
                    .setPositiveButton(R.string.add_meeting_activity_discard_meeting_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.add_meeting_activity_discard_meeting_no, null)
                    .show();
        } else {
            finish();
        }
    }

    private void initUI() { // init UI components
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater()); // bind views with view binding
        View view = mBinding.getRoot();
        setContentView(view);

        setUpAddMeetingButtons();
        refreshAddButton();
        setUpSubjectEditText();
        setUpParticipantsEditText();
        setUpDatePicker();
        setUpTimePicker();
        refreshEndTimeDisplay(mStartTime);
        setUpMeetingRoomSpinner();
        setUpMeetingPrioritySpinner();
    }

    private void refreshAddButton() { // refreshes the add meeting button state (disabled or enabled)
        mBinding.addMeetingActivityAddButton.setEnabled(mSubjectFilledIn && mParticipantsFilledIn && mRoomFilledIn && mPriorityFilledIn);
    }

    void setUpAddMeetingButtons() { // sets-up Buttons
        mBinding.addMeetingActivityBackButton.setOnClickListener(new View.OnClickListener() { // back button
            @Override
            public void onClick(View v) {
                onBackPressed(); // see onBackPressed()
            }
        });
        mBinding.addMeetingActivityAddButton.setOnClickListener(new View.OnClickListener() { // add meeting button
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    void setUpSubjectEditText() { // sets-up the subject EditText
        mBinding.addMeetingActivitySubjectEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSubjectFilledIn = (s.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshAddButton();
            }
        });
    }

    void setUpParticipantsEditText() { // sets-up the participants EditText
        mBinding.addMeetingActivityParticipantsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mParticipantsFilledIn = (s.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshAddButton();
            }
        });
    }

    void setUpDatePicker() { // sets-up the DatePicker
        mBinding.addMeetingActivityDatePickerStartButton.setText(getDateToString(mStartTime));
        mBinding.addMeetingActivityDatePickerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "add_meeting_datePicker");
            }
        });
    }

    void setUpTimePicker() { // sets-up the TimePicker
        mBinding.addMeetingActivityTimePickerStartButton.setText(getTimeToString(mStartTime));
        mBinding.addMeetingActivityTimePickerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "add_meeting_timePicker");
            }
        });
    }

    void setUpMeetingRoomSpinner() { // sets-up the spinner to choose a meeting room
        List<String> meetingRooms = new ArrayList<>();
        meetingRooms.add(0, getString(R.string.hint_meeting_rooms)); // add hint
        for (int resourceString : MeetingRoom.getMeetingRoomsStringResources()) {
            meetingRooms.add(getString(resourceString));
        }
        final ArrayAdapter<String> meetingRoomSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meetingRooms) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // disable hint on dropdown menu
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) { // set hint looks on dropdown menu
                    textView.setTextAppearance(textView.getContext(), R.style.hintItemStyle);
                } else {
                    textView.setTextAppearance(textView.getContext(), R.style.spinnerDropDownItemStyle);
                }
                return view;
            }
        };
        meetingRoomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.addMeetingActivityRoomSpinner.setAdapter(meetingRoomSpinnerAdapter);
        mBinding.addMeetingActivityRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                if (textView != null) {
                    if (position == 0) { // set hint looks on init
                        textView.setTextAppearance(textView.getContext(), R.style.hintItemStyle);
                    } else {
                        textView.setTextAppearance(textView.getContext(), R.style.spinnerItemStyle);
                    }
                }

                mRoomFilledIn = (position != 0);
                refreshAddButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void setUpMeetingPrioritySpinner() { // sets-up the spinner to choose a meeting priority
        List<String> meetingPriorities = new ArrayList<>();
        meetingPriorities.add(0, getString(R.string.hint_meeting_priority)); // add hint
        for (int resourceString : MeetingPriority.getMeetingPrioritiesStringResources()) {
            meetingPriorities.add(getString(resourceString));
        }
        final ArrayAdapter<String> meetingPrioritySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meetingPriorities) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0; // disable hint on dropdown menu
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) { // set hint looks on dropdown menu
                    textView.setTextAppearance(textView.getContext(), R.style.hintItemStyle);
                } else {
                    textView.setTextAppearance(textView.getContext(), R.style.spinnerDropDownItemStyle);
                }
                return view;
            }
        };
        meetingPrioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.addMeetingActivityPrioritySpinner.setAdapter(meetingPrioritySpinnerAdapter);
        mBinding.addMeetingActivityPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                if (textView != null) {
                    if (position == 0) { // set hint looks on init
                        textView.setTextAppearance(textView.getContext(), R.style.hintItemStyle);
                    } else {
                        textView.setTextAppearance(textView.getContext(), R.style.spinnerItemStyle);
                    }
                }

                mPriorityFilledIn = (position != 0);
                refreshAddButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // TODO : make a utils package
    private String getDateToString(Calendar calendar) {
        return new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    private String getTimeToString(Calendar calendar) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
    }

    private void refreshEndTimeDisplay(Calendar startTime) {
        mEndTime = (Calendar) startTime.clone();
        mEndTime.add(Calendar.HOUR, 1);
        mBinding.addMeetingActivityTimePickerEndButton.setText(getTimeToString(mEndTime));
        mBinding.addMeetingActivityDatePickerEndButton.setText(getDateToString(mEndTime));
    }

    private void onSubmit() {
        String subject = mBinding.addMeetingActivitySubjectEditText.getText().toString();
        List<String> participants = new ArrayList<>(Arrays.asList(mBinding.addMeetingActivityParticipantsEditText.getText().toString().trim().split("\\s*,\\s*")));
        MeetingRoom meetingRoom = MeetingRoom.getMeetingRoomByPosition(mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        MeetingPriority meetingPriority = MeetingPriority.getMeetingPriorityByPosition(mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        Meeting newMeeting = new Meeting(mStartTime, subject, participants, meetingRoom, meetingPriority);
        newMeeting.setNotes(mBinding.addMeetingActivityNotesEditText.getText().toString());

        // check if new meeting time slot does not interfere with another meeting
        for (Meeting meeting : DI.getMeetingRepository().getMeetings()) {
            if (newMeeting.getMeetingRoom() == meeting.getMeetingRoom()
                    && newMeeting.getStartTime().getTimeInMillis() < meeting.getEndTime().getTimeInMillis()
                    && newMeeting.getEndTime().getTimeInMillis() > meeting.getStartTime().getTimeInMillis()) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.time_slot_taken_title)
                        .setMessage(getResources().getString(R.string.time_slot_taken_message, getTimeToString(meeting.getStartTime()), getTimeToString(meeting.getEndTime())))
                        .setNeutralButton(R.string.ok, null)
                        .show();
                return;
            }
        }

        String newMeetingJson = new Gson().toJson(newMeeting);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(NEW_MEETING_EXTRA, newMeetingJson);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}