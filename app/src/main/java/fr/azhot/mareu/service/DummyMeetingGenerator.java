package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;

public class DummyMeetingGenerator {

    private final static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(createCalendar(2020, Calendar.JUNE, 1, 8, 0), "Réunion A", Arrays.asList("maxime@lamzone.com", "alex@lamzone.com", "viviane@lamzone.com"), MeetingRoom.MARIO, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 2, 9, 0), "Réunion B", Arrays.asList("paul@lamzone.com", "viviane@lamzone.com", "amandine@lamzone.com"), MeetingRoom.LUIGI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 3, 10, 0), "Réunion C", Arrays.asList("amandine@lamzone.com", "luc@lamzone.com", "jean@lamzone.com"), MeetingRoom.PEACH, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 4, 11, 0), "Réunion D", Arrays.asList("camille@lamzone.com", "amelie@lamzone.com", "pierre@lamzone.com"), MeetingRoom.TOAD, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 5, 12, 0), "Réunion E", Arrays.asList("jean@lamzone.com", "pierre@lamzone.com", "luc@lamzone.com"), MeetingRoom.YOSHI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 6, 13, 0), "Réunion F", Arrays.asList("pierre@lamzone.com", "jean@lamzone.com", "camille@lamzone.com"), MeetingRoom.DAISY, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 7, 14, 0), "Réunion G", Arrays.asList("amelie@lamzone.com", "camille@lamzone.com", "amandine@lamzone.com"), MeetingRoom.BOWSER, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 8, 15, 0), "Réunion H", Arrays.asList("luc@lamzone.com", "amandine@lamzone.com", "paul@lamzone.com"), MeetingRoom.HARMONIE, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 9, 16, 0), "Réunion I", Arrays.asList("viviane@lamzone.com", "paul@lamzone.com", "maxime@lamzone.com"), MeetingRoom.WALUIGI, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 10, 17, 0), "Réunion J", Arrays.asList("alex@lamzone.com", "maxime@lamzone.com", "pierre@lamzone.com"), MeetingRoom.WARIO, MeetingPriority.LOW)
    );

    /**
     * Constructs an instance of {@link Calendar}
     *
     * @param year       an {@link Integer} representing the year set
     * @param month      an {@link Integer} representing the month set (starts at 0 (January) and ends at 11 (December))
     * @param dayOfMonth an {@link Integer} representing the day set
     * @param hour       an {@link Integer} representing the hour set (uses 24-hour clock)
     * @param minute     an {@link Integer} representing the minute set
     * @return an instance of {@link Calendar} with the specified parameters
     */
    private static Calendar createCalendar(final int year, final int month, final int dayOfMonth, final int hour, final int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * @return the static list DUMMY_MEETINGS
     */
    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
