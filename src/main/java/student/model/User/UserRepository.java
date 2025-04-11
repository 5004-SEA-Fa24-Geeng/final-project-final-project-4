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
    User getUserById(UUID id);
    void addUser(User user);
    User findUserByName(String name);
}