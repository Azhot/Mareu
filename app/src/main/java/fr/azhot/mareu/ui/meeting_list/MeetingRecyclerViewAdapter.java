package fr.azhot.mareu.ui.meeting_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.azhot.mareu.databinding.CellMeetingBinding;
import fr.azhot.mareu.models.Meeting;

public class MeetingRecyclerViewAdapter extends RecyclerView.Adapter<MeetingViewHolder> {

    private final Listener mCallback;

    private List<Meeting> mMeetings;

    public MeetingRecyclerViewAdapter(List<Meeting> meetings, Listener callback) {
        this.mMeetings = meetings;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetingViewHolder(CellMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final MeetingViewHolder meetingViewHolder, final int position) {
        final Meeting meeting = mMeetings.get(position);
        meetingViewHolder.initUiElements(meeting, mCallback);
    }

    public void setMeetingsList(List<Meeting> meetings) {
        mMeetings = meetings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public void removeMeetingFromMeetingsList(Meeting meeting) {
        mMeetings.remove(meeting);
        notifyDataSetChanged();
    }

    public interface Listener {
        void onClickDeleteButton(Meeting meeting);

        void onClickMeeting(Meeting meeting);
    }
}