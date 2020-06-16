package fr.azhot.mareu.ui.meeting_add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.R;
import fr.azhot.mareu.base.BaseActivity;
import fr.azhot.mareu.databinding.ActivityAddMeetingBinding;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.utils.CustomTextWatcher;

import static fr.azhot.mareu.utils.TimeUtils.getDateToString;
import static fr.azhot.mareu.utils.TimeUtils.getTimeToString;
import static fr.azhot.mareu.utils.TimeUtils.setTimeOfDay;

public class AddMeetingActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String NEW_MEETING_EXTRA = "new_meeting";
    private static ActivityAddMeetingBinding mBinding;
    private Calendar mStartTimeCalendar;
    private Calendar mEndTimeCalendar;
    private View mClickedView;

    public static void refreshAddButton() { // refreshes the add meeting button state (disabled or enabled)
        mBinding.addMeetingActivityAddButton.setEnabled(mBinding.addMeetingActivitySubjectEditText.length() != 0
                && mBinding.addMeetingActivityParticipantsEditText.length() != 0
                && mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0
                && mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCalendars();
        initUI();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mClickedView.getId() == R.id.add_meeting_activity_start_datePicker_textView) { // user clicked on start date
            mStartTimeCalendar.set(year, month, dayOfMonth);
            mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar));
            refreshEndTimeDisplay();
        } else { // user clicked on end date
            mEndTimeCalendar.set(year, month, dayOfMonth);
            mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar));
            refreshStartTimeDisplay();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mClickedView.getId() == R.id.add_meeting_activity_start_timePicker_textView) { // user clicked on start time
            setTimeOfDay(mStartTimeCalendar, hourOfDay, minute);
            mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar));
            refreshEndTimeDisplay();
        } else { // user clicked on end time
            setTimeOfDay(mEndTimeCalendar, hourOfDay, minute);
            mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar));
            refreshStartTimeDisplay();
        }
    }

    @Override
    public void onBackPressed() { // override onBackPressed to set-up an AlertDialog if user tries to back without saving
        if (mBinding.addMeetingActivitySubjectEditText.length() != 0
                || mBinding.addMeetingActivityParticipantsEditText.length() != 0
                || mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0
                || mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.discard_meeting)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            finish(); // if user did not input anything material, finish activity
        }
    }
    private void initCalendars() {
        mStartTimeCalendar = Calendar.getInstance();
        mStartTimeCalendar.add(Calendar.HOUR, 1);
        mStartTimeCalendar.set(Calendar.MINUTE, 0);
        mStartTimeCalendar.set(Calendar.SECOND, 0);
        mStartTimeCalendar.set(Calendar.MILLISECOND, 0);
        mEndTimeCalendar = (Calendar) mStartTimeCalendar.clone();
        mEndTimeCalendar.add(Calendar.MINUTE, 45);
    }

    private void initUI() { // init UI components
        if (!getResources().getBoolean(R.bool.isTablet)) { // adapt UI depending on device
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setUpButtons();
        refreshAddButton();
        setUpEditTexts();
        setUpDatePickers();
        setUpTimePickers();
        setUpSpinner(getString(R.string.hint_meeting_rooms), MeetingRoom.getMeetingRoomsStringResources(), mBinding.addMeetingActivityRoomSpinner); // set-up meeting rooms spinner
        setUpSpinner(getString(R.string.hint_meeting_priorities), MeetingPriority.getMeetingPrioritiesStringResources(), mBinding.addMeetingActivityPrioritySpinner); // set-up meeting priority spinner
    }

    private void setUpButtons() { // sets-up the "save" and "back" buttons
        mBinding.addMeetingActivityBackButton.setOnClickListener(new View.OnClickListener() { // back button
            @Override
            public void onClick(View v) {
                onBackPressed(); // see onBackPressed() comment
            }
        });
        mBinding.addMeetingActivityAddButton.setOnClickListener(new View.OnClickListener() { // add meeting button
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void setUpEditTexts() { // sets-up the "subject" and "participants" editTexts
        mBinding.addMeetingActivitySubjectEditText.addTextChangedListener(new CustomTextWatcher(mBinding.addMeetingActivitySubjectEditText));
        mBinding.addMeetingActivityParticipantsEditText.addTextChangedListener(new CustomTextWatcher(mBinding.addMeetingActivityParticipantsEditText));
    }

    private void setUpDatePickers() { // sets-up the start date and end date DatePickers
        mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar)); // meeting start date datePicker
        mBinding.addMeetingActivityStartDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mStartTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "add_meeting_start_datePicker");
            }
        });
        mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar)); // meeting end date datePicker
        mBinding.addMeetingActivityEndDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mEndTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "add_meeting_end_datePicker");
            }
        });
    }

    private void setUpTimePickers() { // sets-up the start time and end time TimePickers
        mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar)); // meeting start time timePicker
        mBinding.addMeetingActivityStartTimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(mStartTimeCalendar);
                timePicker.show(getSupportFragmentManager(), "add_meeting_start_timePicker");
            }
        });
        mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar)); // meeting end time timePicker
        mBinding.addMeetingActivityEndTimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(mEndTimeCalendar);
                timePicker.show(getSupportFragmentManager(), "add_meeting_end_timePicker");
            }
        });
    }

    private void setUpSpinner(String hint, List<Integer> e, AppCompatSpinner spinner) { // used to set-up the MeetingRoom and MeetingPriority spinners
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(0, hint); // add hint
        for (int resourceString : e) {
            spinnerList.add(getString(resourceString));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList) {
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
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                refreshAddButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void refreshEndTimeDisplay() { // refreshes end time (+45 minutes) if startTime + 2700000 milliseconds (45 min) > endTime
        if ((mStartTimeCalendar.getTimeInMillis() + 2700000) > mEndTimeCalendar.getTimeInMillis()) {
            mEndTimeCalendar = (Calendar) mStartTimeCalendar.clone();
            mEndTimeCalendar.add(Calendar.MINUTE, 45);
            mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar));
            mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar));
        }
    }

    private void refreshStartTimeDisplay() { // refreshes start time (-45 minutes) if startTime > endTime
        if (mStartTimeCalendar.getTimeInMillis() > mEndTimeCalendar.getTimeInMillis()) {
            mStartTimeCalendar = (Calendar) mEndTimeCalendar.clone();
            mStartTimeCalendar.add(Calendar.MINUTE, -45);
            mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar));
            mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar));
        }
    }

    private void onSubmit() { // fired when clicking on "save" button
        String subject = mBinding.addMeetingActivitySubjectEditText.getText().toString();
        List<String> participants = new ArrayList<>(Arrays.asList(mBinding.addMeetingActivityParticipantsEditText.getText().toString().trim().split("\\s*,\\s*")));
        // check whether participants emails are valid
        for (String participant : participants) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(participant).matches()) {
                Toast toast = Toast.makeText(this, "\"" + participant + "\" " + getString(R.string.invalid_email), Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        MeetingRoom meetingRoom = MeetingRoom.getMeetingRoomByPosition(mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        MeetingPriority meetingPriority = MeetingPriority.getMeetingPriorityByPosition(mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        String notes = mBinding.addMeetingActivityNotesEditText.getText().toString();
        Meeting newMeeting = new Meeting(mStartTimeCalendar, mEndTimeCalendar, subject, participants, meetingRoom, meetingPriority, notes);
        // check if new meeting time slot does not interfere with another meeting
        for (Meeting meeting : getMeetingRepository().getMeetings()) {
            if (newMeeting.getMeetingRoom() == meeting.getMeetingRoom()
                    && newMeeting.getStartTime().getTimeInMillis() < meeting.getEndTime().getTimeInMillis()
                    && newMeeting.getEndTime().getTimeInMillis() > meeting.getStartTime().getTimeInMillis()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.time_slot_taken_title)
                        .setMessage(getResources().getString(R.string.time_slot_taken_message, meeting.getSubject(), getDateToString(meeting.getStartTime()), getTimeToString(meeting.getStartTime())))
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