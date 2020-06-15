package fr.azhot.mareu.utils;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;

import fr.azhot.mareu.R;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class CreateMeetingActions {

    public static void createMeeting(String subject, String participants, int year, int month, int dayOfMonth, int hour, int minute, int positionRoomSpinner, int positionPrioritySpinner) {
        // perform a click on the "add meeting" fab
        onView(withId(R.id.list_meeting_activity_fab))
                .perform(click());
        // fill in subject
        onView(withId(R.id.add_meeting_activity_subject_editText))
                .perform(typeText(subject));
        // fill in participants
        onView(withId(R.id.add_meeting_activity_participants_editText))
                .perform(typeText(participants));
        // close keyboard or otherwise can't click on spinner
        closeSoftKeyboard();
        // set date
        onView(withId(R.id.add_meeting_activity_start_datePicker_textView))
                .perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
        // set time
        onView(withId(R.id.add_meeting_activity_start_timePicker_textView))
                .perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
        // click on room spinner and select first item
        onView(withId(R.id.add_meeting_activity_room_spinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class))))
                .atPosition(positionRoomSpinner)
                .perform(click());
        // click on priority spinner and select first item
        onView(withId(R.id.add_meeting_activity_priority_spinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class))))
                .atPosition(positionPrioritySpinner)
                .perform(click());
        // click on add meeting button
        onView(withId(R.id.add_meeting_activity_add_button))
                .perform(click());
    }
}
