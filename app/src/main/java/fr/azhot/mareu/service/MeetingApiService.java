package fr.azhot.mareu.service;

import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.models.Meeting;

public interface MeetingApiService {

    /**
     * @return the {@link MeetingApiService} meetings list
     */
    List<Meeting> getMeetings();

    /**
     * @param meeting the instance of {@link Meeting} to add to the MeetingApiService meetings list
     */
    void createMeeting(final Meeting meeting);

    /**
     * @param meeting the instance of {@link Meeting} to remove from the MeetingApiService meetings list
     */
    void deleteMeeting(final Meeting meeting);

    /**
     * @param calendar a {@link Calendar} instance
     * @return a list of {@link Meeting} filtered by the {@param calendar} (Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH)
     */
    List<Meeting> getMeetingsFilteredByDate(final Calendar calendar);

    /**
     * @param id an {@link Integer} representing a resource string id corresponding to a meeting room name
     * @return a list of {@link Meeting} filtered by the {@param id}
     */
    List<Meeting> getMeetingsFilteredByRoom(final int id);

}
