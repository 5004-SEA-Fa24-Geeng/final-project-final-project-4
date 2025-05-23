package student.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Provides user-related services by interacting with a UserRepository.
 */
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified UserRepository.
     *
     * @param userRepository the repository to use for accessing user data
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of users
     */
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID, or {@code null} if not found
     */
    public User getUserById(UUID id) {
        for (User user : getUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Attempts to log in a user by name.
     *
     * @param name the name of the user to log in
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User login(String name) {
        return userRepository.findUserByName(name);
    }

    /**
     * Registers a new user by name.
     * <p>
     * Throws an exception if a user with the same name already exists.
     * </p>
     *
     * @param name the name of the new user
     * @return the newly created {@link User}
     * @throws IllegalStateException if a user with the same name already exists
     */
    public User register(String name) {
        User existing = userRepository.findUserByName(name);
        if (existing != null) {
            throw new IllegalStateException("User already exists with name: " + name);
        }
        User newUser = new User(UUID.randomUUID(), name);
        userRepository.addUser(newUser);
        return newUser;
    }
}