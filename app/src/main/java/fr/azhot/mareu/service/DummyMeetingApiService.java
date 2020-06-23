package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.models.Meeting;

public class DummyMeetingApiService implements MeetingApiService {

    private final List<Meeting> mMeetings = DummyMeetingGenerator.generateMeetings();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings() {
        return mMeetings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMeeting(final Meeting meeting) {
        mMeetings.add(meeting);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMeeting(final Meeting meeting) {
        mMeetings.remove(meeting);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetingsFilteredByDate(final Calendar calendar) {
        List<Meeting> meetings = new ArrayList<>();
        for (Meeting meeting : mMeetings) {
            if (meeting.getStartTime().getTimeInMillis() >= calendar.getTimeInMillis()
                    && meeting.getStartTime().getTimeInMillis() < (calendar.getTimeInMillis() + 86400000)
                    || meeting.getEndTime().getTimeInMillis() >= calendar.getTimeInMillis()
                    && meeting.getEndTime().getTimeInMillis() < (calendar.getTimeInMillis() + 86400000)
                    || meeting.getStartTime().getTimeInMillis() <= calendar.getTimeInMillis()
                    && meeting.getEndTime().getTimeInMillis() >= (calendar.getTimeInMillis() + 86400000)) {
                meetings.add(meeting);
            }
        }
        return meetings;
    }

    /**
     * {@inheritDoc}
     */
    public List<Meeting> getMeetingsFilteredByRoom(final int id) {
        List<Meeting> meetings = new ArrayList<>();
        for (Meeting meeting : mMeetings) {
            if (meeting.getMeetingRoom().getStringResource() == id) {
                meetings.add(meeting);
            }
        }
        return meetings;
    }
}
