package fr.azhot.mareu.meeting_list;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import fr.azhot.mareu.R;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.CreateMeetingActions;
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Instrumented test of filtering meetings by date
 */
@RunWith(AndroidJUnit4.class)
public class FilterByDateMeetingTest {

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
     * When we filter by date, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByDateAction_shouldOnlyDisplayFilteredItems() {

        /* first add a meeting at distant date to make sure that at least one meeting exists */
        CreateMeetingActions.createMeeting("MeetingTest", "test@test.test", 1992, 1, 1, 12, 0, 1, 1);

        /* then count occurrences in mMeetings for this date */
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

    @After
    public void tearDown() {
        mActivityRule.finishActivity();
    }
}
