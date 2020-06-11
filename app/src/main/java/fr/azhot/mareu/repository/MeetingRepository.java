package fr.azhot.mareu.repository;

import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.service.MeetingApiService;

public class MeetingRepository {

    private final MeetingApiService mMeetingApiService;

    /**
     * Constructor for {@link MeetingRepository}
     *
     * @param meetingApiService an instance of a class implementing {@link MeetingApiService}
     */
    public MeetingRepository(MeetingApiService meetingApiService) {
        this.mMeetingApiService = meetingApiService;
    }

    /**
     * Returns the list of meetings of the repository
     *
     * @return a list of {@link Meeting}
     */
    public List<Meeting> getMeetings() {
        return mMeetingApiService.getMeetings();
    }

    /**
     * Adds a meeting to the repository
     *
     * @param meeting a {@link Meeting} instance
     */
    public void createMeeting(final Meeting meeting) {
        mMeetingApiService.createMeeting(meeting);
    }

    /**
     * Removes a meeting from the repository
     *
     * @param meeting a {@link Meeting} instance
     */
    public void deleteMeeting(final Meeting meeting) {
        mMeetingApiService.deleteMeeting(meeting);
    }

    /**
     * Returns a list of meetings filtered by a given date
     *
     * @param calendar a {@link Calendar} instance representing the date filter to apply
     * @return a list of {@link Meeting} which date correspond to the {@param calendar} param
     */
    public List<Meeting> getMeetingsFilteredByDate(final Calendar calendar) {
        return mMeetingApiService.getMeetingsFilteredByDate(calendar);
    }

    /**
     * Returns a list of meetings filtered by a given meeting room
     *
     * @param id a static constant from {@link fr.azhot.mareu.models.MeetingRoom} representing a room
     * @return a list of {@link Meeting} which room correspond to the {@param id} param
     */
    public List<Meeting> getMeetingsFilteredByRoom(final int id) {
        return mMeetingApiService.getMeetingsFilteredByRoom(id);
    }
}
