package fr.azhot.mareu.di;

import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.service.DummyMeetingApiService;

/**
 * Dependency injector to inject {@link MeetingRepository}
 */
public class DI {

    private static MeetingRepository mMeetingRepository = new MeetingRepository(new DummyMeetingApiService());

    /**
     * @return a {@link MeetingRepository}
     */
    public static MeetingRepository getMeetingRepository() {
        return mMeetingRepository;
    }

    /**
     * Creates a new {@link MeetingRepository}, for tests
     *
     * @return a {@link MeetingRepository} instance
     */
    public static MeetingRepository getNewMeetingRepository() {
        return new MeetingRepository(new DummyMeetingApiService());
    }
}
