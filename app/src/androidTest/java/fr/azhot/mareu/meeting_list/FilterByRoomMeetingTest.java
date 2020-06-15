package fr.azhot.mareu.meeting_list;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.azhot.mareu.R;
import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.ui.meeting_list.ListMeetingActivity;
import fr.azhot.mareu.utils.CreateMeetingActions;
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Instrumented test of filtering meetings by date
 */
@RunWith(AndroidJUnit4.class)
public class FilterByRoomMeetingTest {

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
     * When we filter by room, we only get relevant meetings
     */
    @Test
    public void myMeetingsList_filterByRoomAction_shouldOnlyDisplayFilteredItems() {

        /* first create a meeting with this room to make sure that at least one meeting exists */
        CreateMeetingActions.createMeeting("MeetingTest", "test@test.test", 1993, 1, 1, 12, 0, 1, 1);

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
        onView(withText(R.string.mario))
                .perform(click());

        /* finally check that child count  */
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(count));
    }

    @After
    public void tearDown() {
        mActivityRule.finishActivity();
    }
}
