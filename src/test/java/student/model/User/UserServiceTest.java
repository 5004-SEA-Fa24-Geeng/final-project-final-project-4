package student.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize UserService with the static UserArrayRepository before each test
        UserRepository userRepository = new UserArrayRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void testGetUsers_ReturnsAllUsers() {
        // Retrieve all users
        List<User> users = userService.getUsers();

        // Verify the list is not null and contains exactly two users
        assertNotNull(users);
        assertEquals(2, users.size());

        // Verify the first user
        assertEquals("James", users.get(0).getName());
        assertEquals(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), users.get(0).getId());

        // Verify the second user
        assertEquals("Jamila", users.get(1).getName());
        assertEquals(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), users.get(1).getId());
    }

    @Test
    void testGetUserById_FindsCorrectUser() {
        // Test retrieving an existing user by ID
        UUID id = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");
        User user = userService.getUserById(id);

        // Verify the user is found and data is correct
        assertNotNull(user);
        assertEquals("James", user.getName());
        assertEquals(id, user.getId());
    }

    @Test
    void testGetUserById_ReturnsNullForNonexistentUser() {
        // Generate a random UUID not in the list
        UUID unknownId = UUID.randomUUID();
        User user = userService.getUserById(unknownId);

        // Verify that no user is found (null is returned)
        assertNull(user);
    }
}
