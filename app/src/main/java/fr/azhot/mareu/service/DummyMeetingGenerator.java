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
            new Meeting(createCalendar(2020, Calendar.JUNE, 1, 4, 0), "Réunion A", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.MARIO, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 2, 6, 0), "Réunion B", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.LUIGI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 3, 8, 0), "Réunion C", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.PEACH, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 4, 10, 0), "Réunion D", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.TOAD, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 5, 12, 0), "Réunion E", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.YOSHI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 6, 14, 0), "Réunion F", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.DAISY, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 7, 16, 0), "Réunion G", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.BOWSER, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 8, 18, 0), "Réunion H", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.HARMONIE, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 9, 20, 0), "Réunion I", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.WALUIGI, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 10, 22, 0), "Réunion J", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.WARIO, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JULY, 1, 5, 0), "Réunion K", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.MARIO, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JULY, 2, 7, 0), "Réunion L", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.LUIGI, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JULY, 3, 9, 0), "Réunion M", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.PEACH, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JULY, 4, 11, 0), "Réunion N", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.TOAD, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JULY, 5, 13, 0), "Réunion O", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.YOSHI, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JULY, 6, 15, 0), "Réunion P", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.DAISY, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JULY, 7, 17, 0), "Réunion Q", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.BOWSER, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JULY, 8, 19, 0), "Réunion R", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.HARMONIE, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JULY, 9, 21, 0), "Réunion S", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.WALUIGI, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JULY, 10, 23, 0), "Réunion T", Arrays.asList("test@lamazone.com", "test2@lamazone.com", "test3@lamazone.com"), MeetingRoom.WARIO, MeetingPriority.AVERAGE)
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
