package fr.azhot.mareu.ui.meeting_add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import fr.azhot.mareu.R;
import fr.azhot.mareu.base.BaseActivity;
import fr.azhot.mareu.databinding.ActivityAddMeetingBinding;
import fr.azhot.mareu.events.MustRefreshAddButtonEvent;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.utils.MyOnItemSelectedListener;
import fr.azhot.mareu.utils.MyTextWatcher;

import static fr.azhot.mareu.utils.TimeUtils.getDateToString;
import static fr.azhot.mareu.utils.TimeUtils.getTimeToString;
import static fr.azhot.mareu.utils.TimeUtils.initEndCalendar;
import static fr.azhot.mareu.utils.TimeUtils.initStartCalendar;
import static fr.azhot.mareu.utils.TimeUtils.setTimeOfDay;

public class AddMeetingActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String NEW_MEETING_EXTRA = "new_meeting";
    private ActivityAddMeetingBinding mBinding;
    private Calendar mStartTimeCalendar;
    private Calendar mEndTimeCalendar;
    private View mClickedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartTimeCalendar = initStartCalendar();
        mEndTimeCalendar = initEndCalendar(mStartTimeCalendar);
        initUIComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        refreshAvailableMeetingRooms();
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
        refreshAvailableMeetingRooms();
    }

    @Override
    public void onBackPressed() { // override onBackPressed to set-up an AlertDialog if user tries to back without saving
        if (mBinding.addMeetingActivitySubjectEditText.length() + mBinding.addMeetingActivityParticipantsEditText.length() + mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() + mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0) {
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

    private void initUIComponents() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setUpScrollView();
        setUpButtons();
        refreshAddButton();
        setUpEditTexts();
        setUpDatePickers();
        setUpTimePickers();
        setUpSpinner(getString(R.string.hint_meeting_rooms), MeetingRoom.getMeetingRoomsStringResources(), mBinding.addMeetingActivityRoomSpinner); // set-up meeting rooms spinner
        refreshAvailableMeetingRooms();
        setUpSpinner(getString(R.string.hint_meeting_priorities), MeetingPriority.getMeetingPrioritiesStringResources(), mBinding.addMeetingActivityPrioritySpinner); // set-up meeting priority spinner
    }

    private void setUpScrollView() { // sets-up the scrollView to show top border when scrolled-down
        mBinding.addMeetingActivityScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mBinding.addMeetingActivityScrollView.getScrollY() != 0) {
                    mBinding.addMeetingActivityScrollView.setBackground(getDrawable(R.drawable.bg_with_top_border));
                } else {
                    mBinding.addMeetingActivityScrollView.setBackground(getDrawable(R.drawable.bg_with_bottom_border));
                }
            }
        });
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
        mBinding.addMeetingActivitySubjectEditText.addTextChangedListener(new MyTextWatcher());
        mBinding.addMeetingActivityParticipantsEditText.addTextChangedListener(new MyTextWatcher());
    }

    private void setUpDatePickers() { // sets-up the start date and end date DatePickers
        mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar)); // meeting start date DatePicker
        mBinding.addMeetingActivityStartDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mStartTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "start_date_picker");
            }
        });
        mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar)); // meeting end date DatePicker
        mBinding.addMeetingActivityEndDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mEndTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "end_date_picker");
            }
        });
    }

    private void setUpTimePickers() { // sets-up the start time and end time TimePickers
        mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar)); // meeting start time TimePicker
        mBinding.addMeetingActivityStartTimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(mStartTimeCalendar);
                timePicker.show(getSupportFragmentManager(), "start_time_picker");
            }
        });
        mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar)); // meeting end time TimePicker
        mBinding.addMeetingActivityEndTimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(mEndTimeCalendar);
                timePicker.show(getSupportFragmentManager(), "end_time_picker");
            }
        });
    }

    private void setUpSpinner(String hint, List<Integer> stringResources, AppCompatSpinner spinner) { // used to set-up the MeetingRoom and MeetingPriority spinners
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(hint); // add hint
        for (int stringResource : stringResources) {
            spinnerList.add(getString(stringResource));
        }
        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(this, R.layout.spinner_item, spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    private void refreshAvailableMeetingRooms() { // refreshes list of available meeting rooms at a set time slot
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(getString(R.string.hint_meeting_rooms)); // add hint
        for (int stringResource : MeetingRoom.getMeetingRoomsStringResources()) {
            spinnerList.add(getString(stringResource));
        }
        for (Meeting meeting : getMeetingRepository().getMeetings()) {
            for (MeetingRoom meetingRoom : MeetingRoom.values()) {
                if (meetingRoom == meeting.getMeetingRoom() && mStartTimeCalendar.getTimeInMillis() < meeting.getEndTime().getTimeInMillis() && mEndTimeCalendar.getTimeInMillis() > meeting.getStartTime().getTimeInMillis()) {
                    spinnerList.remove(getString(meetingRoom.getStringResource()));
                }
            }
        }
        if (spinnerList.size() == 1) { // list is empty except for hint
            spinnerList.clear();
            spinnerList.add(getString(R.string.no_room_available));
        }
        ((MySpinnerAdapter) mBinding.addMeetingActivityRoomSpinner.getAdapter()).setList(spinnerList);
        mBinding.addMeetingActivityRoomSpinner.setSelection(0);
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

    public void refreshAddButton() { // refreshes the add meeting button state (disabled or enabled)
        mBinding.addMeetingActivityAddButton.setEnabled(mBinding.addMeetingActivitySubjectEditText.length() != 0
                && mBinding.addMeetingActivityParticipantsEditText.length() != 0
                && mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0
                && mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0);
    }

    private void onSubmit() { // fired when clicking on "save" button
        String subject = Objects.requireNonNull(mBinding.addMeetingActivitySubjectEditText.getText(), "Subject editText must not be null").toString();
        List<String> participants = new ArrayList<>(Arrays.asList(Objects.requireNonNull(mBinding.addMeetingActivityParticipantsEditText.getText(), "Participants editText must not be null").toString().trim().split("\\s*,\\s*")));
        for (String participant : participants) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(participant).matches()
                    || !participant.endsWith(getString(R.string.company_domain_name)) // first check if valid email to avoid unnecessary call to API
                    || !getUserRepository().getUsersEmail().contains(participant)) {
                Toast toast = Toast.makeText(this, "\"" + participant + "\" " + getString(R.string.invalid_email), Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        MeetingRoom selectedMeetingRoom = null;
        String stringMeetingRoom = ((MySpinnerAdapter) mBinding.addMeetingActivityRoomSpinner.getAdapter()).getList().get(mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition());
        for (MeetingRoom meetingRoom : MeetingRoom.values()) {
            if (stringMeetingRoom.equals(getString(meetingRoom.getStringResource()))) {
                selectedMeetingRoom = meetingRoom;
                break;
            }
        }
        MeetingPriority meetingPriority = MeetingPriority.getMeetingPriorityByPosition(mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        String notes = Objects.requireNonNull(mBinding.addMeetingActivityNotesEditText.getText(), "Notes editText must not be null").toString();
        Meeting newMeeting = new Meeting(mStartTimeCalendar, mEndTimeCalendar, subject, participants, selectedMeetingRoom, meetingPriority, notes);
        String newMeetingJson = new Gson().toJson(newMeeting);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(NEW_MEETING_EXTRA, newMeetingJson);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Subscribe
    public void onMustRefreshAddButtonEvent(MustRefreshAddButtonEvent event) {
        refreshAddButton();
    }
}