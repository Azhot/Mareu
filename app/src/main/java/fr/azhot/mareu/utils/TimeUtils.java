package fr.azhot.mareu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static Calendar initStartCalendar() {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.add(Calendar.HOUR, 1);
        startTimeCalendar.set(Calendar.MINUTE, 0);
        startTimeCalendar.set(Calendar.SECOND, 0);
        startTimeCalendar.set(Calendar.MILLISECOND, 0);
        return startTimeCalendar;
    }

    public static Calendar initEndCalendar(Calendar startTimeCalendar) {
        Calendar endTimeCalendar = (Calendar) startTimeCalendar.clone();
        endTimeCalendar.add(Calendar.MINUTE, 45);
        return endTimeCalendar;
    }

    public static void setTimeOfDay(Calendar calendar, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static String getDateToString(final Calendar calendar) {
        return new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    public static String getTimeToString(final Calendar calendar) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
    }
}
