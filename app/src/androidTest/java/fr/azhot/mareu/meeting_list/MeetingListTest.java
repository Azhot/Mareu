package fr.azhot.mareu.meeting_list;

import androidx.test.espresso.contrib.RecyclerViewActions;
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
import fr.azhot.mareu.utils.RecyclerViewItemCountAssertion;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static fr.azhot.mareu.utils.MyViewAction.clickChildViewWithId;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Instrumented test for list of meetings
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

        // reset recyclerview list to cope with other tests
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_no_filter))
                .perform(click());
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

    @After
    public void tearDown() {
        mActivity.resetMeetingRepository();
    }
}


