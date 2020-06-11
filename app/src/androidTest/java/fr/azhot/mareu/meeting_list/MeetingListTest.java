package fr.azhot.mareu.meeting_list;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import fr.azhot.mareu.R;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static fr.azhot.mareu.utils.MyViewAction.clickChildViewWithId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Instrumented test for list of meetings
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListTest {

    private static int ITEMS_COUNT = DI.getNewMeetingRepository().getMeetings().size();

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule = new ActivityTestRule(ListMeetingActivity.class);
    private ListMeetingActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mActivity.setMeetingRepository(DI.getNewMeetingRepository());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myMeetingList_shouldNotBeEmpty() {
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the recyclerview child count is decremented
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        // first check recyclerview child count
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(ITEMS_COUNT));
        // then perform a click on delete icon of first item
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.meeting_cell_delete_button)));
        ITEMS_COUNT--;
        // finally check that child count is decremented
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(ITEMS_COUNT));
    }

    /**
     * When we add an item, the recyclerview child count is incremented
     */
    @Test
    public void myMeetingsList_addMeetingActivity_shouldAddItem() {
        // first create a meeting
        createMeeting("MeetingTest", "test@test.test", 1990, 1, 1, 12, 0, 1, 1, true);

        // then check that child count is incremented
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(ITEMS_COUNT));
    }

    /**
     * When trying to add a meeting which overlaps another meeting, an AlterDialog is displayed
     */
    @Test
    public void myMeetingList_displayAlertDialogIfOverlaps() {

        /* first create a meeting at a definite date (distant), time and room  */
        createMeeting("MeetingTest", "test@test.test", 1991, 1, 1, 12, 0, 1, 1, true);

        /* then try to create a meeting at the same date, time and room */
        createMeeting("MeetingTest2", "test2@test.test", 1991, 1, 1, 12, 0, 1, 3, false);

        /* finally check that the dialog is displayed */
        onView(withText(R.string.time_slot_taken_title)).check(matches(isDisplayed()));
    }

    /**
     * When we filter by date, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByDateAction_shouldOnlyDisplayFilteredItems() {

        /* first add a meeting at distant date to make sure that at least one meeting exists */
        createMeeting("MeetingTest", "test@test.test", 1992, 1, 1, 12, 0, 1, 1, true);

        /* then count occurrences in mMeetings for a definite date (distant) */
        int count = 0;
        for (Meeting meeting : mActivity.getMeetingRepository().getMeetings()) {
            if (meeting.getStartTime().get(Calendar.YEAR) == 1992
                    && meeting.getStartTime().get(Calendar.DAY_OF_YEAR) == 1) {
                count++;
            }
        }

        /* then select filter */
        // open the menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // click on date filter
        onView(withText(R.string.menu_filter_by_date))
                .perform(click());
        // set date
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1992, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());

        /* finally check that child count is 1 */
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(count));
    }

    /**
     * When we filter by room, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByRoomAction_shouldOnlyDisplayFilteredItems() {

        /* first create a meeting with this room to make sure that at least one meeting exists */
        createMeeting("MeetingTest", "test@test.test", 1993, 1, 1, 12, 0, 1, 1, true);

        /* then count occurrences in mMeetings for this room */
        int count = 0;
        for (Meeting meeting : mActivity.getMeetingRepository().getMeetings()) {
            if (meeting.getMeetingRoom() == MeetingRoom.MARIO) {
                count++;
            }
        }

        /* then select filter */
        // open the menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // click on date filter
        onView(withText(R.string.menu_filter_by_meeting_room))
                .perform(click());
        onView(withText(R.string.mario_meeting_room))
                .perform(click());

        /* finally check that child count  */
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(count));
    }

    // todo : class utils
    // todo : have a separate class for each test
    public void createMeeting(String subject, String participants, int year, int month, int dayOfMonth, int hour, int minute, int positionRoomSpinner, int positionPrioritySpinner, boolean iterate) {
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
        onView(withId(R.id.add_meeting_activity_datePicker_start_button))
                .perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
        // set time
        onView(withId(R.id.add_meeting_activity_timePicker_start_button))
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
        if(iterate) {
            ITEMS_COUNT++;
        }
    }
}


