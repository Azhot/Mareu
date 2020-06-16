package fr.azhot.mareu;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
     * When trying to add a meeting that overlaps another meeting, an AlterDialog is displayed
     */
    @Test
    public void myMeetingList_displayAlertDialogIfOverlaps() {
        // first create a meeting at a definite date (distant), time and room
        CreateMeetingActions.createMeeting();

        // then try to create a meeting at the same date, time and room
        CreateMeetingActions.createMeeting();

        // finally check that the AlertDialog is displayed
        onView(withText(R.string.time_slot_taken_title)).check(matches(isDisplayed()));
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
