package student.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A repository that provides users from a static array.
 */
public class UserArrayRepository implements UserRepository {

    private static final User[] users;

    static {
        users = new User[]{
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"),
                new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Jamila")
        };
    }

    /**
     * Returns a list of users from a static array.
     *
     * @return a list of users
     */
    @Override
    public List<User> getUsers() {
        return Arrays.asList(users);
    }
}