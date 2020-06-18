package fr.azhot.mareu.models;

import java.util.UUID;

/**
 * Class representing a user
 */
public class User {
    private final String mId;
    private String mEmail;

    /**
     * Constructor for a {@link User}
     *
     * @param email the e-mail address of the user
     */
    public User(String email) {
        this.mId = UUID.randomUUID().toString();
        this.mEmail = email;
    }

    /**
     * @return the user's e-mail address
     */
    public String getEmail() {
        return mEmail;
    }
}
