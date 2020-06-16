package fr.azhot.mareu.meeting_add;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.azhot.mareu.R;
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
 * Instrumented test of adding a meeting
 */
@RunWith(AndroidJUnit4.class)
public class AddMeetingTest {


    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule = new ActivityTestRule(ListMeetingActivity.class);
    private ListMeetingActivity mActivity;
    private int mItemsCount;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mItemsCount = mActivity.getMeetingRepository().getMeetings().size();

        // reset recyclerview list to cope with other tests
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_no_filter))
                .perform(click());
    }

    /**
     * When we add an item, the recyclerview child count is incremented
     */
    @Test
    public void myAddMeeting_addMeetingActivity_shouldAddItem() {
        // first create a meeting
        CreateMeetingActions.createMeeting("MeetingTest", "test@test.test", 1990, 1, 1, 12, 0, 1, 1);

        // then check that child count is incremented
        onView(withId(R.id.list_meeting_activity_recyclerView))
                .check(new RecyclerViewItemCountAssertion(mItemsCount + 1));
    }

    @After
    public void tearDown() {
        mActivity.resetMeetingRepository();
    }
}
