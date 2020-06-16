package fr.azhot.mareu.di;

import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.service.DummyMeetingApiService;

/**
 * Dependency injector to inject {@link MeetingRepository}
 */
public class DI {

    /**
     * @return a {@link MeetingRepository}
     */
    public static MeetingRepository createMeetingRepository() {
        return new MeetingRepository(new DummyMeetingApiService());
    }
}
