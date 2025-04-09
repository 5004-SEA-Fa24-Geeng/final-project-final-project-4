package student.model.User;

import java.util.List;

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
}