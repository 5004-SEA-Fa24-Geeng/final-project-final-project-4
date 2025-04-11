package student.model.User;

import java.io.*;
import java.util.*;

public class UserFileRepository implements UserRepository {

    private static final String USER_FILE_PATH = "data/users.csv";

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

    @Override
    public void addUser(User user) {
        try (FileWriter writer = new FileWriter(USER_FILE_PATH, true)) {
            writer.write(user.getId() + "," + user.getName() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("❌ Failed to write user to users.csv", e);
        }
    }

    @Override
    public User getUserById(UUID id) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public User findUserByName(String name) {
        return getUsers().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
