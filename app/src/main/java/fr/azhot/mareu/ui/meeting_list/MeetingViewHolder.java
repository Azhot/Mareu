package fr.azhot.mareu.ui.meeting_list;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

import fr.azhot.mareu.R;
import fr.azhot.mareu.databinding.CellMeetingBinding;
import fr.azhot.mareu.models.Meeting;

public class MeetingViewHolder extends RecyclerView.ViewHolder {

    private final String TAG = MeetingViewHolder.class.getSimpleName();
    private CellMeetingBinding mBinding;
    private WeakReference<MeetingRecyclerViewAdapter.Listener> mCallback;

    public MeetingViewHolder(CellMeetingBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void initUiElements(final Meeting meeting, MeetingRecyclerViewAdapter.Listener callback) {
        mCallback = new WeakReference<>(callback);
        mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingRecyclerViewAdapter.Listener callback = mCallback.get();
                if (mCallback.get() != null) mCallback.get().onClickMeeting(meeting);
            }
        });

        // set-up the priority image color
        switch (meeting.getMeetingPriority()) {
            case LOW:
                mBinding.meetingCellPriorityImageView.setImageResource(R.drawable.ic_low_priority);
                break;
            case AVERAGE:
                mBinding.meetingCellPriorityImageView.setImageResource(R.drawable.ic_average_priority);
                break;
            case HIGH:
                mBinding.meetingCellPriorityImageView.setImageResource(R.drawable.ic_high_priority);
                break;
            default:
                Log.e(TAG, "Cannot set-up the priority of meeting with id : " + meeting.getId());
                break;
        }

        // set-up the meeting details text
        SimpleDateFormat time = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
        StringBuilder meetingDetails = new StringBuilder();
        meetingDetails.append(meeting.getSubject());
        meetingDetails.append(" - ");
        meetingDetails.append(time.format(meeting.getStartTime().getTime()));
        meetingDetails.append(" - ");
        meetingDetails.append(itemView.getContext().getString(meeting.getMeetingRoom().getStringResource()));
        mBinding.meetingCellMeetingTextView.setText(meetingDetails);

        // set-up the participants details text
        StringBuilder participants = new StringBuilder();
        for (int i = 0; i < meeting.getParticipants().size(); i++) {
            participants.append(meeting.getParticipants().get(i).getEmail());
            if (i != meeting.getParticipants().size() - 1) {
                participants.append(", ");
            }
        }
        mBinding.meetingCellParticipantsTextView.setText(participants);

        // set-up the delete image button
        mBinding.meetingCellDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback.get() != null) mCallback.get().onClickDeleteButton(meeting);
            }
        });
    }
}
