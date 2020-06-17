package fr.azhot.mareu.events;

import fr.azhot.mareu.MaReuApplication;
import fr.azhot.mareu.base.BaseActivity;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.repository.MeetingRepository;

public class DeleteMeetingEvent extends BaseActivity {

    public DeleteMeetingEvent(Meeting meeting) {
        MeetingRepository meetingRepository = ((MaReuApplication) getApplication()).getMeetingRepository();
        meetingRepository.deleteMeeting(meeting);
    }
}
