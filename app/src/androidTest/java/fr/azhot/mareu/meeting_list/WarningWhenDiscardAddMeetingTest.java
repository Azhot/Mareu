package fr.azhot.mareu.meeting_list;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.azhot.mareu.R;
import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.CreateMeetingActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Instrumented test for adding a meeting
 */
@RunWith(AndroidJUnit4.class)
public class WarningWhenDiscardAddMeetingTest {

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule = new ActivityTestRule(ListMeetingActivity.class);
    private ListMeetingActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * When trying to add a meeting which overlaps another meeting, an AlterDialog is displayed
     */
    @Test
    public void myMeetingList_displayAlertDialogIfOverlaps() {

        /* first create a meeting at a definite date (distant), time and room  */
        CreateMeetingActions.createMeeting("MeetingTest", "test@test.test", 1991, 1, 1, 12, 0, 1, 1);

        /* then try to create a meeting at the same date, time and room */
        CreateMeetingActions.createMeeting("MeetingTest2", "test2@test.test", 1991, 1, 1, 12, 0, 1, 3);

        /* finally check that the dialog is displayed */
        onView(withText(R.string.time_slot_taken_title)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        mActivity.resetMeetingRepository();
    }
}
