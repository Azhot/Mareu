package fr.azhot.mareu.ui.meeting_list;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.azhot.mareu.R;
import fr.azhot.mareu.databinding.ActivityListMeetingBinding;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.ui.meeting_add.AddMeetingActivity;
import fr.azhot.mareu.ui.meeting_add.DatePickerFragment;

public class ListMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int LAUNCH_ADD_MEETING_ACTIVITY = 1;
    private ActivityListMeetingBinding mBinding;
    private MeetingRepository mMeetingRepository;
    private List<Meeting> mMeetings;
    private MeetingRecyclerViewAdapter mMeetingRecyclerViewAdapter;
    private ActionBar mActionBar;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        // create room sub-menu items programmatically (so it automatically updates if we update meeting rooms)
        SubMenu roomSubMenu = menu.findItem(R.id.room_filter).getSubMenu();
        for (int i = 0; i < MeetingRoom.getMeetingRoomsStringResources().size(); i++) {
            roomSubMenu.add(R.id.room_filter, MeetingRoom.getMeetingRoomsStringResources().get(i), Menu.NONE, MeetingRoom.getMeetingRoomsStringResources().get(i));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.no_filter:
                setAdapterMeetingList(mMeetingRepository.getMeetings());
                setActionBarSubtitle(null);
                break;
            case R.id.date_filter:
                DialogFragment datePicker = new DatePickerFragment(mCalendar);
                datePicker.show(getSupportFragmentManager(), "filter_datePicker");
                break;
            default:
                if (MeetingRoom.getMeetingRoomsStringResources().contains(item.getItemId())) {
                    setAdapterMeetingList(mMeetingRepository.getMeetingsFilteredByRoom(item.getItemId()));
                    setActionBarSubtitle(getString(R.string.filter) + ": " + item.getTitle());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        setAdapterMeetingList(mMeetingRepository.getMeetingsFilteredByDate(mCalendar));
        SimpleDateFormat date = new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault());
        setActionBarSubtitle(getString(R.string.filter) + ": " + date.format(mCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // retrieve new meeting via Gson
        if (requestCode == LAUNCH_ADD_MEETING_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            String newMeetingGson = data.getStringExtra(AddMeetingActivity.NEW_MEETING_EXTRA);
            Meeting newMeeting = new Gson().fromJson(newMeetingGson, Meeting.class);
            mMeetingRepository.createMeeting(newMeeting);
            if (mMeetings != mMeetingRepository.getMeetings()) {
                mMeetings.add(newMeeting); // to cope with updating list if a filter is on
            }
            mMeetingRecyclerViewAdapter.setMeetingList(mMeetings);
        }
    }

    private void initData() {
        mMeetingRepository = DI.getMeetingRepository();
        mMeetings = mMeetingRepository.getMeetings();
        mActionBar = getSupportActionBar();
        mCalendar = Calendar.getInstance();
    }

    private void initUI() {
        // bind widgets with view binding
        mBinding = ActivityListMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        // set-up the RecyclerView
        mBinding.listMeetingActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMeetingRecyclerViewAdapter = new MeetingRecyclerViewAdapter(this, mMeetings);
        mBinding.listMeetingActivityRecyclerView.setAdapter(mMeetingRecyclerViewAdapter);
        // set-up the FAB to add new meetings
        mBinding.listMeetingActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), AddMeetingActivity.class), LAUNCH_ADD_MEETING_ACTIVITY);
            }
        });
    }

    // used to apply a subtitle to the action bar when selecting a filter
    private void setActionBarSubtitle(String string) {
        if (mActionBar != null) {
            mActionBar.setSubtitle(string);
        }
    }

    // used for filtering meetings
    private void setAdapterMeetingList(List<Meeting> meetings) {
        mMeetings = meetings;
        mMeetingRecyclerViewAdapter.setMeetingList(mMeetings);
    }

    // for tests
    public MeetingRepository getMeetingRepository() {
        return this.mMeetingRepository;
    }

    // todo : implement base activity
    // for tests
    public void setMeetingRepository(MeetingRepository meetingRepository) {
        this.mMeetingRepository = meetingRepository;
    }
}