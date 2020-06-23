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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

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

    // override onBackPressed to set-up an AlertDialog if user tries to back without saving
    @Override
    public void onBackPressed() {
        if (mBinding.addMeetingActivitySubjectEditText.length()
                + mBinding.addMeetingActivityParticipantsEditText.length()
                + mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition()
                + mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0) {
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
        setUpDatePicker(mBinding.addMeetingActivityStartDatePickerTextView, mStartTimeCalendar);
        setUpTimePicker(mBinding.addMeetingActivityStartTimePickerTextView, mStartTimeCalendar);
        setUpDatePicker(mBinding.addMeetingActivityEndDatePickerTextView, mEndTimeCalendar);
        setUpTimePicker(mBinding.addMeetingActivityEndTimePickerTextView, mEndTimeCalendar);
        setUpSpinner(getString(R.string.hint_meeting_rooms), MeetingRoom.getMeetingRoomsStringResources(), mBinding.addMeetingActivityRoomSpinner); // set-up meeting rooms spinner
        refreshAvailableMeetingRooms();
        setUpSpinner(getString(R.string.hint_meeting_priorities), MeetingPriority.getMeetingPrioritiesStringResources(), mBinding.addMeetingActivityPrioritySpinner); // set-up meeting priority spinner
    }

    // sets-up the scrollView to show top border when scrolled-down
    private void setUpScrollView() {
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

    // used to set-up the save and back buttons
    private void setUpSaveAndBackButtons() {
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

    // used to set-up subject and participants editTexts
    private void setUpEditText(AppCompatEditText editText) {
        editText.addTextChangedListener(new MyTextWatcher());
    }

    // used to set-up the start and end datePickers
    private void setUpDatePicker(AppCompatTextView textView, final Calendar calendar) {
        textView.setText(getDateToString(calendar));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment datePicker = new DatePickerFragment(calendar);
                datePicker.show(getSupportFragmentManager(), datePicker.getTag());
            }
        });
    }

    // used to set-up the start and end timePickers
    private void setUpTimePicker(AppCompatTextView textView, final Calendar calendar) {
        textView.setText(getTimeToString(calendar));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                DialogFragment timePicker = new TimePickerFragment(calendar);
                timePicker.show(getSupportFragmentManager(), timePicker.getTag());
            }
        });
    }

    // used to set-up the MeetingRoom and MeetingPriority spinners
    private void setUpSpinner(String hint, List<Integer> stringResources, AppCompatSpinner spinner) {
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(hint); // add hint
        for (int stringResource : stringResources) {
            spinnerList.add(getString(stringResource));
        }
        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(this, R.layout.spinner_item, spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    // refreshes the list of available meeting rooms at a set time slot
    private void refreshAvailableMeetingRooms() {
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(getString(R.string.hint_meeting_rooms)); // add hint
        for (int stringResource : MeetingRoom.getMeetingRoomsStringResources()) {
            spinnerList.add(getString(stringResource));
        }
        for (Meeting meeting : getMeetingRepository().getMeetings()) {
            for (MeetingRoom meetingRoom : MeetingRoom.values()) {
                if (meetingRoom == meeting.getMeetingRoom()
                        && mStartTimeCalendar.getTimeInMillis() < meeting.getEndTime().getTimeInMillis()
                        && mEndTimeCalendar.getTimeInMillis() > meeting.getStartTime().getTimeInMillis()) {
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

    // refreshes end time (+45 minutes) if endTime < startTime + 2700000 milliseconds (45 min)
    private void refreshEndTimeDisplay() {
        if ((mStartTimeCalendar.getTimeInMillis() + 2700000) > mEndTimeCalendar.getTimeInMillis()) {
            mEndTimeCalendar = (Calendar) mStartTimeCalendar.clone();
            mEndTimeCalendar.add(Calendar.MINUTE, 45);
            mBinding.addMeetingActivityEndDatePickerTextView.setText(getDateToString(mEndTimeCalendar));
            mBinding.addMeetingActivityEndTimePickerTextView.setText(getTimeToString(mEndTimeCalendar));
        }
    }

    // refreshes the start time (-45 minutes) if startTime < endTime
    private void refreshStartTimeDisplay() {
        if (mStartTimeCalendar.getTimeInMillis() > mEndTimeCalendar.getTimeInMillis()) {
            mStartTimeCalendar = (Calendar) mEndTimeCalendar.clone();
            mStartTimeCalendar.add(Calendar.MINUTE, -45);
            mBinding.addMeetingActivityStartDatePickerTextView.setText(getDateToString(mStartTimeCalendar));
            mBinding.addMeetingActivityStartTimePickerTextView.setText(getTimeToString(mStartTimeCalendar));
        }
    }

    // refreshes the add meeting button state (disabled or enabled) depending on whether user input all necessary fields
    public void refreshAddButton() {
        mBinding.addMeetingActivityAddButton.setEnabled(mBinding.addMeetingActivitySubjectEditText.length() != 0
                && mBinding.addMeetingActivityParticipantsEditText.length() != 0
                && mBinding.addMeetingActivityRoomSpinner.getSelectedItemPosition() != 0
                && mBinding.addMeetingActivityPrioritySpinner.getSelectedItemPosition() != 0);
    }

    // fired when clicking on "save" button -> creates the meeting and leads back to the list meeting activity
    private void onSubmit() {
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
        getMeetingRepository().createMeeting(new Meeting(mStartTimeCalendar, mEndTimeCalendar, subject, participants, meetingRoom, meetingPriority, notes));
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Subscribe
    public void onMustRefreshAddButtonEvent(MustRefreshAddButtonEvent event) {
        refreshAddButton();
    }
}