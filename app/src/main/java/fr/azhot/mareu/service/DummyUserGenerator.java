package fr.azhot.mareu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.azhot.mareu.models.User;

public class DummyUserGenerator {

    private final static List<User> DUMMY_USERS = Arrays.asList(
            new User("maxime@lamzone.com"),
            new User("paul@lamzone.com"),
            new User("amandine@lamzone.com"),
            new User("camille@lamzone.com"),
            new User("jean@lamzone.com"),
            new User("pierre@lamzone.com"),
            new User("amelie@lamzone.com"),
            new User("luc@lamzone.com"),
            new User("viviane@lamzone.com"),
            new User("alex@lamzone.com")
    );


    /**
     * @return the static list DUMMY_USERS
     */
    public static List<User> generateUsers() {
        return new ArrayList<>(DUMMY_USERS);
    }
}
