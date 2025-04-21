package student.model.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserFileRepositoryTest {

    private UserFileRepository repository;
    private static final String TEST_FILE_PATH = "data/users.csv";

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();

        repository = new UserFileRepository();
    }

    @AfterEach
    void tearDown() throws IOException {
        // 每次测试后，清理测试数据
        Files.deleteIfExists(new File(TEST_FILE_PATH).toPath());
    }

    @Test
    void addUsersAndGetUsers() {
        User user = new User(UUID.randomUUID(), "Alice");
        repository.addUser(user);

        List<User> users = repository.getUsers();
        assertEquals(1, users.size());
        assertEquals("Alice", users.get(0).getName());
    }

    @Test
    void getUsersReturnsEmptyListWhenFileDoesNotExist() throws IOException {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        UserFileRepository repository = new UserFileRepository();
        List<User> users = repository.getUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void getUsersIgnoresMalformedLines() throws IOException {
        File file = new File(TEST_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("onlyonefield\n");
        }

        UserFileRepository repository = new UserFileRepository();
        List<User> users = repository.getUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void getUsersThrowsExceptionWhenFileIsBroken() throws IOException {
        File file = new File(TEST_FILE_PATH);
        file.delete();
        file.mkdirs();

        UserFileRepository repository = new UserFileRepository();

        assertThrows(IllegalStateException.class, repository::getUsers);

        file.delete();
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    @Test
    void addUserThrowsExceptionWhenFileIsBroken() throws IOException {
        File file = new File(TEST_FILE_PATH);
        file.delete();
        file.mkdirs();

        UserFileRepository repository = new UserFileRepository();
        User user = new User(UUID.randomUUID(), "TestUser");

        assertThrows(IllegalStateException.class, () -> repository.addUser(user));

        file.delete();
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    @Test
    void getUserById() {
        User user = new User(UUID.randomUUID(), "Bob");
        repository.addUser(user);

        User found = repository.getUserById(user.getId());
        assertNotNull(found);
        assertEquals("Bob", found.getName());
    }

    @Test
    void testGetUserByIdNotFound() {
        User found = repository.getUserById(UUID.randomUUID());
        assertNull(found);
    }

    @Test
    void findUserByName() {
        User user = new User(UUID.randomUUID(), "Charlie");
        repository.addUser(user);

        User found = repository.findUserByName("Charlie");
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    void findUserByNameCaseInsensitive() {
        User user = new User(UUID.randomUUID(), "David");
        repository.addUser(user);

        User found = repository.findUserByName("david");
        assertNotNull(found);
        assertEquals("David", found.getName());
    }

    @Test
    void findUserByNameNotFound() {
        User found = repository.findUserByName("Nonexistent");
        assertNull(found);
    }
}