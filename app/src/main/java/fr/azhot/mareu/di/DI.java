package fr.azhot.mareu.di;

import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.repository.UserRepository;
import fr.azhot.mareu.service.DummyMeetingApiService;
import fr.azhot.mareu.service.DummyUserApiService;

/**
 * Dependency injector to inject repositories
 */
public class DI {

    /**
     * @return a {@link MeetingRepository}
     */
    public static MeetingRepository createMeetingRepository() {
        return new MeetingRepository(new DummyMeetingApiService());
    }

    /**
     * @return a {@link UserRepository}
     */
    public static UserRepository createUserRepository() {
        return new UserRepository(new DummyUserApiService());
    }
}
