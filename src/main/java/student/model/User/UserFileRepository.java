package student.model.User;

import java.io.*;
import java.util.*;

/**
 * File-based implementation of the {@link UserRepository} interface.
 * <p>
 * This class manages persistent user storage using a CSV file located at {@code data/users.csv}.
 * Each user record is stored in the format: {@code UUID,name}.
 * </p>
 */
public class UserFileRepository implements UserRepository {

    /**
     * The relative file path to the user data CSV file.
     */
    private static final String USER_FILE_PATH = "data/users.csv";

    /**
     * Reads all users from the CSV file.
     *
     * @return a list of {@link User} objects loaded from the file; empty list if file not found
     * @throws IllegalStateException if the file cannot be read
     */
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE_PATH);

        if (!file.exists()) return users;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(",");
                if (split.length >= 2) {
                    users.add(new User(UUID.fromString(split[0]), split[1]));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("❌ Failed to read users.csv", e);
        }

        return users;
    }

    /**
     * Appends a new user to the CSV file.
     *
     * @param user the {@link User} to be saved
     * @throws IllegalStateException if the file cannot be written
     */
    @Override
    public void addUser(User user) {
        try (FileWriter writer = new FileWriter(USER_FILE_PATH, true)) {
            writer.write(user.getId() + "," + user.getName() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("❌ Failed to write user to users.csv", e);
        }
    }

    /**
     * Searches for a user in the file by their UUID.
     *
     * @param id the UUID to search for
     * @return the matching {@link User}, or {@code null} if not found
     */
    @Override
    public User getUserById(UUID id) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Searches for a user in the file by their name (case-insensitive).
     *
     * @param name the username to search
     * @return the matching {@link User}, or {@code null} if not found
     */
    @Override
    public User findUserByName(String name) {
        return getUsers().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
