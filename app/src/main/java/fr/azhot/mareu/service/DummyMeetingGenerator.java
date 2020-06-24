package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.models.User;

public class DummyMeetingGenerator {

    private final static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(createCalendar(2020, Calendar.JUNE, 1, 8, 0), "Réunion A", Arrays.asList(new User("maxime@lamzone.com"), new User("alex@lamzone.com"), new User("viviane@lamzone.com")), MeetingRoom.MARIO, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 2, 9, 0), "Réunion B", Arrays.asList(new User("paul@lamzone.com"), new User("viviane@lamzone.com"), new User("amandine@lamzone.com")), MeetingRoom.LUIGI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 3, 10, 0), "Réunion C", Arrays.asList(new User("amandine@lamzone.com"), new User("luc@lamzone.com"), new User("jean@lamzone.com")), MeetingRoom.PEACH, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 4, 11, 0), "Réunion D", Arrays.asList(new User("camille@lamzone.com"), new User("amelie@lamzone.com"), new User("pierre@lamzone.com")), MeetingRoom.TOAD, MeetingPriority.LOW),
            new Meeting(createCalendar(2020, Calendar.JUNE, 5, 12, 0), "Réunion E", Arrays.asList(new User("jean@lamzone.com"), new User("pierre@lamzone.com"), new User("luc@lamzone.com")), MeetingRoom.YOSHI, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 6, 13, 0), "Réunion F", Arrays.asList(new User("pierre@lamzone.com"), new User("jean@lamzone.com"), new User("camille@lamzone.com")), MeetingRoom.DAISY, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 7, 14, 0), "Réunion G", Arrays.asList(new User("amelie@lamzone.com"), new User("camille@lamzone.com"), new User("amandine@lamzone.com")), MeetingRoom.BOWSER, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 8, 15, 0), "Réunion H", Arrays.asList(new User("luc@lamzone.com"), new User("amandine@lamzone.com"), new User("paul@lamzone.com")), MeetingRoom.HARMONIE, MeetingPriority.AVERAGE),
            new Meeting(createCalendar(2020, Calendar.JUNE, 9, 16, 0), "Réunion I", Arrays.asList(new User("viviane@lamzone.com"), new User("paul@lamzone.com"), new User("maxime@lamzone.com")), MeetingRoom.WALUIGI, MeetingPriority.HIGH),
            new Meeting(createCalendar(2020, Calendar.JUNE, 10, 17, 0), "Réunion J", Arrays.asList(new User("alex@lamzone.com"), new User("maxime@lamzone.com"), new User("pierre@lamzone.com")), MeetingRoom.WARIO, MeetingPriority.LOW)
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
