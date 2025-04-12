package student.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserArrayRepositoryTest {

    private UserArrayRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserArrayRepository();
    }

    @Test
    void getUsers() {
        List<User> users = repository.getUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void getUserById() {
        User expectedUser = repository.getUsers().get(0);
        User foundUser = repository.getUserById(expectedUser.getId());
        assertNotNull(foundUser);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void getUserByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        User foundUser = repository.getUserById(randomId);
        assertNull(foundUser);
    }

    @Test
    void addUser() {
        User newUser = new User(UUID.randomUUID(), "Emily");
        repository.addUser(newUser);
        assertTrue(repository.getUsers().contains(newUser));
    }

    @Test
    void findUserByName() {
        User foundUser = repository.findUserByName("James");
        assertNotNull(foundUser);
        assertEquals("James", foundUser.getName());
    }

    @Test
    void findUserByNameCaseInsensitive() {
        User foundUser = repository.findUserByName("jamila");
        assertNotNull(foundUser);
        assertEquals("Jamila", foundUser.getName());
    }

    @Test
    void findUserByNameNotFound() {
        User foundUser = repository.findUserByName("Nonexistent");
        assertNull(foundUser);
    }
}