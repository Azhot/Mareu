package fr.azhot.mareu.repository;

import java.util.List;

import fr.azhot.mareu.models.User;
import fr.azhot.mareu.service.UserApiService;

public class UserRepository {

    private final UserApiService mUserApiService;

    /**
     * Constructor for {@link UserRepository}
     *
     * @param userApiService an instance of a class implementing {@link UserApiService}
     */
    public UserRepository(UserApiService userApiService) {
        this.mUserApiService = userApiService;
    }

    /**
     * Returns the list of users of the repository
     *
     * @return a list of {@link User}
     */
    public List<User> getUsers() {
        return mUserApiService.getUsers();
    }

    /**
     * Returns the list of users of the repository
     *
     * @return a list of {@link User}
     */
    public List<String> getUsersEmail() {
        return mUserApiService.getUsersEmail();
    }
}
