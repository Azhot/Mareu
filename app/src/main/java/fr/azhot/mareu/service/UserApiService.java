package fr.azhot.mareu.service;

import java.util.List;

import fr.azhot.mareu.models.User;

public interface UserApiService {

    /**
     * @return the {@link UserApiService} users list
     */
    List<User> getUsers();

    /**
     * @return the {@link UserApiService} users email list
     */
    List<String> getUsersEmail();

}
