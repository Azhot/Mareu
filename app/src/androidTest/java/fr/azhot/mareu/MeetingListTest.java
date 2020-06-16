package fr.azhot.mareu;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.CreateMeetingActions;
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static fr.azhot.mareu.utils.MyViewAction.clickChildViewWithId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Instrumented test for ListMeetingActivity
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListTest {

    private int mItemsCount;

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule = new ActivityTestRule(ListMeetingActivity.class);
    private ListMeetingActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mItemsCount = mActivity.getMeetingRepository().getMeetings().size();
    }

    @After
    public void tearDown() {
        mActivity.resetMeetingRepository();
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
                .check(new RecyclerViewItemCountAssertion(mItemsCount));
        // then perform a click on delete icon of first item
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.meeting_cell_delete_button)));
        // finally check that child count is decremented
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(mItemsCount - 1));
    }

    /**
     * When we filter by room, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByRoomAction_shouldOnlyDisplayFilteredItems() {

        /* first create a meeting in Mario room just to make sure that at least one meeting exists */
        CreateMeetingActions.createMeeting();

        /* then count occurrences in repository for Mario room */
        int count = 0;
        for (Meeting meeting : mActivity.getMeetingRepository().getMeetings()) {
            if (meeting.getMeetingRoom() == MeetingRoom.MARIO) {
                count++;
            }
        }

        /* then select filter for Mario room */
        // open the menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // click on date filter
        onView(withText(R.string.menu_filter_by_meeting_room))
                .perform(click());
        onView(withText(R.string.mario))
                .perform(click());

        /* finally check that child count matches */
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(count));
    }

    /**
     * When we filter by date, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByDateAction_shouldOnlyDisplayFilteredItems() {

        /* first add a meeting at a distant date to make sure that at least one meeting exists
         * and that it does not overlap any potentially existing meeting */
        CreateMeetingActions.createMeeting();

        /* then count occurrences in repository for this date */
        int count = 0;
        for (Meeting meeting : mActivity.getMeetingRepository().getMeetings()) {
            if (meeting.getStartTime().get(Calendar.YEAR) == 1990
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
                .perform(PickerActions.setDate(1990, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());

        /* finally check that child count matches */
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(count));
    }
}


