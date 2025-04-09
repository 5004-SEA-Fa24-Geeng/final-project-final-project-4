package student.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * A repository that loads users from a CSV file.
 */
public class UserFileRepository implements UserRepository {

    /**
     * Reads users from a CSV file located at {@code src/student/users.csv}.
     *
     * @return a list of users
     * @throws IllegalStateException if the file cannot be read
     */
    @Override
    public List<User> getUsers() {
        File file = new File("src/student/users.csv");

        List<User> users = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                users.add(new User(UUID.fromString(split[0]), split[1]));
            }
            return users;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}