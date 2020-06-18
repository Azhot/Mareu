package fr.azhot.mareu;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.CreateMeetingActions;
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Instrumented test of AddMeetingActivity
 */
@RunWith(AndroidJUnit4.class)
public class AddMeetingTest {

    @Rule
    public final ActivityTestRule<ListMeetingActivity> mActivityRule = new ActivityTestRule(ListMeetingActivity.class);
    private int mItemsCount;
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
     * When we add a meeting, the recyclerview child count is incremented
     */
    @Test
    public void myAddMeeting_addMeetingActivity_shouldAddItem() {
        // first create a meeting
        CreateMeetingActions.createMeeting();

        // then check that child count is incremented
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(mItemsCount + 1));
    }

    /**
     * When trying to add a meeting that overlaps another meeting, the MeetingRoom spinner is updated
     */
    @Test
    public void myMeetingList_displayAlertDialogIfOverlaps() {
        // first create a meeting at a definite date (distant), time and room
        CreateMeetingActions.createMeeting();

        // then start to create a meeting at the same date, time
        // perform a click on the "add meeting" fab
        onView(withId(R.id.list_meeting_activity_fab))
                .perform(click());
        // set date
        onView(withId(R.id.add_meeting_activity_end_datePicker_textView))
                .perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1990, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());
        // set time
        onView(withId(R.id.add_meeting_activity_end_timePicker_textView))
                .perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(12, 45));
        onView(withId(android.R.id.button1)).perform(click());

        // finally check that the MeetingRoom spinner does not show Mario room anymore
        onView(withId(R.id.add_meeting_activity_room_spinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class))))
                .atPosition(1)
                .perform(click());
        onView(withId(R.id.add_meeting_activity_room_spinner)).check(matches(not(withSpinnerText(R.string.mario))));
    }

    /**
     * When hitting back button whilst adding a meeting, an AlterDialog is displayed
     */
    @Test
    public void myMeetingList_displayAlertDialogIfDiscard() {
        // first perform a click on the "add meeting" fab
        onView(withId(R.id.list_meeting_activity_fab))
                .perform(click());
        // then fill in subject
        onView(withId(R.id.add_meeting_activity_subject_editText))
                .perform(typeText("MeetingTest"));
        // then hit the back button
        onView(withId(R.id.add_meeting_activity_back_button))
                .perform(click());
        // finally check that the AlertDialog is displayed
        onView(withText(R.string.discard_meeting)).check(matches(isDisplayed()));
    }
}
