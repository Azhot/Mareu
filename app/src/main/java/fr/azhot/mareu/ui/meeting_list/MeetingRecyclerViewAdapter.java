package fr.azhot.mareu.ui.meeting_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fr.azhot.mareu.R;
import fr.azhot.mareu.databinding.CellMeetingBinding;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;

public class MeetingRecyclerViewAdapter extends RecyclerView.Adapter<MeetingRecyclerViewAdapter.MeetingViewHolder> {

    private final Context mContext;
    private List<Meeting> mMeetings;

    public MeetingRecyclerViewAdapter(Context context, List<Meeting> meetings) {
        this.mContext = context;
        this.mMeetings = meetings;
    }

    public void setMeetingList(List<Meeting> meetings) {
        mMeetings = meetings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetingViewHolder(CellMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, final int position) {
        Meeting meeting = mMeetings.get(position);
        holder.initUiElements(meeting);
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }


    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = MeetingViewHolder.class.getSimpleName();
        private CellMeetingBinding mBinding;

        public MeetingViewHolder(final CellMeetingBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        private void initUiElements(final Meeting meeting) {

            /* set-up the priority image color */
            switch (meeting.getMeetingPriority()) {
                case LOW:
                    mBinding.meetingCellPriorityImage.setImageResource(R.drawable.ic_low_priority_50);
                    break;
                case AVERAGE:
                    mBinding.meetingCellPriorityImage.setImageResource(R.drawable.ic_average_priority_50);
                    break;
                case HIGH:
                    mBinding.meetingCellPriorityImage.setImageResource(R.drawable.ic_high_priority_50);
                    break;
                default:
                    Log.e(TAG, "Cannot set-up the priority of meeting with id : " + meeting.getId());
                    break;
            }

            /* set-up the meeting details text */
            SimpleDateFormat time = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
            StringBuilder meetingDetails = new StringBuilder();
            meetingDetails.append(meeting.getSubject());
            meetingDetails.append(" - ");
            meetingDetails.append(time.format(meeting.getStartTime().getTime()));
            meetingDetails.append(" : ");
            meetingDetails.append(time.format(meeting.getEndTime().getTime()));
            meetingDetails.append(" - ");
            meetingDetails.append(mContext.getString(meeting.getMeetingRoom().getStringResource()));
            mBinding.meetingCellMeetingText.setText(meetingDetails);

            /* set-up the participants details text */
            StringBuilder participants = new StringBuilder();
            for (int i = 0; i < meeting.getParticipants().size(); i++) {
                participants.append(meeting.getParticipants().get(i));
                if (i != meeting.getParticipants().size() - 1) {
                    participants.append(", ");
                }
            }
            mBinding.meetingCellParticipantsText.setText(participants);

            /* set-up the delete image button */
            mBinding.meetingCellDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DI.getMeetingRepository().deleteMeeting(meeting);
                    mMeetings.remove(meeting); // to cope with updating filtered lists
                    notifyDataSetChanged();
                }
            });
        }
    }
}
