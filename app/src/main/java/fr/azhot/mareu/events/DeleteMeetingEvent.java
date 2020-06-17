package fr.azhot.mareu.events;

import android.content.Context;

import fr.azhot.mareu.base.BaseActivity;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.repository.MeetingRepository;

public class DeleteMeetingEvent {

    public DeleteMeetingEvent(Context context, Meeting meeting) {
        BaseActivity activity = (BaseActivity) context;
        MeetingRepository meetingRepository = activity.getMeetingRepository();
        meetingRepository.deleteMeeting(meeting);
    }
}
