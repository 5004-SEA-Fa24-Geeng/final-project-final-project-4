package student.model.User;

import java.util.*;

/**
 * In-memory implementation of the {@link UserRepository} interface.
 * <p>
 * This repository stores user data in a simple {@link ArrayList},
 * making it useful for testing or scenarios that don't require persistence.
 * </p>
 * <p>
 * The repository is initialized with two predefined users.
 * </p>
 */
public class UserArrayRepository implements UserRepository {

    /**
     * Internal list storing all user records.
     */
    private final List<User> users = new ArrayList<>();

    /**
     * Constructs the repository and initializes it with sample users.
     */
    public UserArrayRepository() {
        users.add(new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"));
        users.add(new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Jamila"));
    }

    /**
     * Retrieves all users in the repository.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getUsers() {
        return users;
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param id the ID of the user to find
     * @return the matching {@link User}, or {@code null} if not found
     */
    @Override
    public User getUserById(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to add
     */
    @Override
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Searches for a user by name (case-insensitive).
     *
     * @param name the name to search for
     * @return the matching {@link User}, or {@code null} if not found
     */
    @Override
    public User findUserByName(String name) {
        return users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
