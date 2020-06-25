package fr.azhot.mareu;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.models.User;
import fr.azhot.mareu.repository.UserRepository;
import fr.azhot.mareu.service.DummyUserGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Unit test on User repository
 */
@RunWith(JUnit4.class)
public class UserRepositoryTest {

    UserRepository mUserRepository;

    @Before
    public void setup() {
        mUserRepository = DI.createUserRepository();
    }

    @Test
    public void getUsersWithSuccess() {
        List<User> users = mUserRepository.getUsers();
        List<User> expectedUsers = DummyUserGenerator.generateUsers();
        assertThat(users, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedUsers.toArray()));
        assertEquals(users.size(), expectedUsers.size());
    }

    @Test
    public void getUsersEmailWithSuccess() {
        List<String> usersEmail = mUserRepository.getUsersEmail();
        List<User> expectedUsers = DummyUserGenerator.generateUsers();
        List<String> expectedUsersEmail = new ArrayList<>();
        for (User user : expectedUsers) {
            expectedUsersEmail.add(user.getEmail());
        }
        assertThat(usersEmail, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedUsersEmail.toArray()));
        assertEquals(usersEmail.size(), expectedUsersEmail.size());
    }
}
