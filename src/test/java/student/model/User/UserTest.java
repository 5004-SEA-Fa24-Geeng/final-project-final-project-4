package student.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        user = new User(id, "Alice");
    }

    @Test
    void getId() {
        assertEquals(id, user.getId());
    }

    @Test
    void setId() {
        UUID newId = UUID.randomUUID();
        user.setId(newId);
        assertEquals(newId, user.getId());
    }

    @Test
    void getName() {
        assertEquals("Alice", user.getName());
    }

    @Test
    void setName() {
        user.setName("Bob");
        assertEquals("Bob", user.getName());

    }

    @Test
    void testToString() {
        String str = user.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains(id.toString()));
    }

    @Test
    void testEquals() {
        User sameUser = new User(id, "Alice");
        User differentUser = new User(UUID.randomUUID(), "Charlie");

        assertEquals(user, user);
        assertEquals(user, sameUser);
        assertNotEquals(user, differentUser);
        assertNotEquals(user, new Object());

        User sameIdDifferentName = new User(id, "DifferentName");
        assertNotEquals(user, sameIdDifferentName);

        User sameNameDifferentId = new User(UUID.randomUUID(), "Alice");
        assertNotEquals(user, sameNameDifferentId);
    }

    @Test
    void testHashCode() {
        User sameUser = new User(id, "Alice");
        assertEquals(user.hashCode(), sameUser.hashCode());
    }
}