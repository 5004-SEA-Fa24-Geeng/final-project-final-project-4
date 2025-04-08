package student.model.User;

import java.util.List;

/**
 * A repository interface for retrieving {@link User} objects.
 * <p>
 * Implementations of this interface provide different data sources,
 * such as in-memory arrays, files, or databases.
 * </p>
 */
public interface UserRepository {

    /**
     * Returns a list of all users available from the data source.
     *
     * @return a list of {@link User} objects
     */
    List<User> getUsers();
}
