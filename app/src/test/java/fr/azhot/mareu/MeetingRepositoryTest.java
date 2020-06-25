package fr.azhot.mareu;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.Meeting;
import fr.azhot.mareu.models.MeetingPriority;
import fr.azhot.mareu.models.MeetingRoom;
import fr.azhot.mareu.models.User;
import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.service.DummyMeetingGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Meeting repository
 */
@RunWith(JUnit4.class)
public class MeetingRepositoryTest {

    MeetingRepository mMeetingRepository;

    @Before
    public void setup() {
        mMeetingRepository = DI.createMeetingRepository();
    }

    @Test
    public void getMeetingsWithSuccess() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        List<Meeting> expectedMeetings = DummyMeetingGenerator.generateMeetings();
        assertThat(meetings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMeetings.toArray()));
        assertEquals(meetings.size(), expectedMeetings.size());
    }

    @Test
    public void createMeetingWithSuccess() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        Meeting meeting = new Meeting(Calendar.getInstance(), "MeetingTest", Collections.singletonList(new User("test@test.test")), MeetingRoom.MARIO, MeetingPriority.AVERAGE);
        mMeetingRepository.createMeeting(meeting);
        assertTrue(meetings.contains(meeting));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        Meeting meeting = new Meeting(Calendar.getInstance(), "MeetingTest", Collections.singletonList(new User("test@test.test")), MeetingRoom.MARIO, MeetingPriority.AVERAGE);
        meetings.add(meeting);
        mMeetingRepository.deleteMeeting(meeting);
        assertFalse(meetings.contains(meeting));
    }

    @Test
    public void getMeetingsFilteredByDateWithSuccess() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        for (Meeting meeting : meetings) {
            meeting.getStartTime().set(2020, Calendar.JUNE, 1, 12, 0, 0);
            meeting.getEndTime().set(2020, Calendar.JUNE, 1, 13, 0, 0);
        }
        meetings.get(0).getStartTime().set(2020, Calendar.JUNE, 2, 12, 0, 0);
        meetings.get(0).getEndTime().set(2020, Calendar.JUNE, 2, 13, 0, 0);
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.JUNE, 2, 12, 0, 0);
        List<Meeting> expectedMeetings = mMeetingRepository.getMeetingsFilteredByDate(cal);
        assertEquals(1, expectedMeetings.size());
        assertTrue(expectedMeetings.get(0).getStartTime().get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
                && expectedMeetings.get(0).getStartTime().get(Calendar.YEAR) == cal.get(Calendar.YEAR));
    }

    @Test
    public void getMeetingsFilteredByRoomWithSuccess() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        for (Meeting meeting : meetings) {
            meeting.setMeetingRoom(MeetingRoom.MARIO);
        }
        meetings.get(0).setMeetingRoom(MeetingRoom.LUIGI);
        List<Meeting> expectedMeetings = mMeetingRepository.getMeetingsFilteredByRoom(MeetingRoom.LUIGI.getStringResource());
        assertEquals(1, expectedMeetings.size());
        assertEquals(expectedMeetings.get(0).getMeetingRoom(), MeetingRoom.LUIGI);
    }
}
