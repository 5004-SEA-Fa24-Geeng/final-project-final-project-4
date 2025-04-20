package student.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Defines methods for accessing user data.
 */
public interface UserRepository {

    /**
     * Retrieves a list of users.
     *
     * @return a list of users
     */
    List<User> getUsers();

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the UUID of the user
     * @return the matching {@link User}, or {@code null} if not found
     */
    User getUserById(UUID id);

    /**
     * Adds a new user to the data source.
     *
     * @param user the user to add
     */
    void addUser(User user);

    /**
     * Finds a user by name (case-insensitive).
     *
     * @param name the name to search for
     * @return the matching {@link User}, or {@code null} if not found
     */
    User findUserByName(String name);
}