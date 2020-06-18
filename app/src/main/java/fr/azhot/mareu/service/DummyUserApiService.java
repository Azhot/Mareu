package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.List;

import fr.azhot.mareu.models.User;

public class DummyUserApiService implements UserApiService {

    private final List<User> mUsers = DummyUserGenerator.generateUsers();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        return mUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getUsersEmail() {
        List<String> emails = new ArrayList<>();
        for (User user : mUsers) {
            emails.add(user.getEmail());
        }
        return emails;
    }
}
