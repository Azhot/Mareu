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

import androidx.appcompat.widget.AppCompatEditText;
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
    private Calendar mStartTimeCalendar, mEndTimeCalendar;
    private View mClickedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
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
    // override onBackPressed to set-up an AlertDialog if user tries to back without saving
    public void onBackPressed() {
        if (mBinding.addMeetingActivitySubjectEditText.length() != 0 && mBinding.addMeetingActivityParticipantsEditText.length() != 0 && mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0 && mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0) {
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

    private void initData() {
        mStartTimeCalendar = initStartCalendar();
        mEndTimeCalendar = initEndCalendar(mStartTimeCalendar);
    }

    private void initUIComponents() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUpScrollView();
        setUpSaveAndBackButtons();
        refreshAddButton();
        setUpEditText(mBinding.addMeetingActivitySubjectEditText);
        setUpEditText(mBinding.addMeetingActivityParticipantsEditText);
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

    private void setUpSaveAndBackButtons() { // used to set-up the save and back buttons
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

    private void setUpEditText(AppCompatEditText editText) { // used to set-up subject and participants editTexts
        editText.addTextChangedListener(new MyTextWatcher());
    }

    private void setUpDatePickers() { // used to set-up the start and end datePickers
        mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar));
        mBinding.addMeetingActivityStartDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mStartTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "start_date_picker");
            }
        });
        mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar));
        mBinding.addMeetingActivityEndDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(mEndTimeCalendar);
                datePicker.show(getSupportFragmentManager(), "end_date_picker");
            }
        });
    }

    private void setUpTimePickers() { // used to set-up the start and end timePickers
        mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar));
        mBinding.addMeetingActivityStartTimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(mStartTimeCalendar);
                timePicker.show(getSupportFragmentManager(), "start_time_picker");
            }
        });
        mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar));
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

    private void refreshAvailableMeetingRooms() { // refreshes the list of available meeting rooms at a set time slot
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

    private void refreshEndTimeDisplay() { // refreshes end time (+45 minutes) if endTime < startTime + 2700000 milliseconds (45 min)
        if ((mStartTimeCalendar.getTimeInMillis() + 2700000) > mEndTimeCalendar.getTimeInMillis()) {
            mEndTimeCalendar = (Calendar) mStartTimeCalendar.clone();
            mEndTimeCalendar.add(Calendar.MINUTE, 45);
            mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar));
            mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar));
        }
    }

    private void refreshStartTimeDisplay() { // refreshes the start time (-45 minutes) if startTime < endTime
        if (mStartTimeCalendar.getTimeInMillis() > mEndTimeCalendar.getTimeInMillis()) {
            mStartTimeCalendar = (Calendar) mEndTimeCalendar.clone();
            mStartTimeCalendar.add(Calendar.MINUTE, -45);
            mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar));
            mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar));
        }
    }

    public void refreshAddButton() { // refreshes the add meeting button state (disabled or enabled) depending on whether user input all necessary fields
        mBinding.addMeetingActivityAddButton.setEnabled(mBinding.addMeetingActivitySubjectEditText.length() != 0 && mBinding.addMeetingActivityParticipantsEditText.length() != 0 && mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0 && mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0);
    }

    private void onSubmit() { // fired when clicking on "save" button -> creates the meeting and leads back to the list meeting activity
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
        MeetingRoom meetingRoom = null;
        String stringMeetingRoom = ((MySpinnerAdapter) mBinding.addMeetingActivityRoomSpinner.getAdapter()).getList().get(mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition());
        for (MeetingRoom m : MeetingRoom.values()) {
            if (stringMeetingRoom.equals(getString(m.getStringResource()))) {
                meetingRoom = m;
                break;
            }
        }
        MeetingPriority meetingPriority = MeetingPriority.getMeetingPriorityByPosition(mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() - 1); // withdraw 1 corresponding to the hint
        String notes = Objects.requireNonNull(mBinding.addMeetingActivityNotesEditText.getText(), "Notes editText must not be null").toString();
        String newMeetingJson = new Gson().toJson(new Meeting(mStartTimeCalendar, mEndTimeCalendar, subject, participants, meetingRoom, meetingPriority, notes));
        Intent resultIntent = new Intent().putExtra(NEW_MEETING_EXTRA, newMeetingJson);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Subscribe
    public void onMustRefreshAddButtonEvent(MustRefreshAddButtonEvent event) {
        refreshAddButton();
    }
}