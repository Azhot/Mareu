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
            if (meeting.getStartTime().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && meeting.getStartTime().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && meeting.getStartTime().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
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
