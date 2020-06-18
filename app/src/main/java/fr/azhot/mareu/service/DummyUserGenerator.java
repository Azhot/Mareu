package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.azhot.mareu.models.User;

public class DummyUserGenerator {

    private final static List<User> DUMMY_USERS = Arrays.asList(
            new User("test@lamazone.com"),
            new User("test2@lamazone.com"),
            new User("test3@lamazone.com")
    );


    /**
     * @return the static list DUMMY_USERS
     */
    public static List<User> generateUsers() {
        return new ArrayList<>(DUMMY_USERS);
    }
}
