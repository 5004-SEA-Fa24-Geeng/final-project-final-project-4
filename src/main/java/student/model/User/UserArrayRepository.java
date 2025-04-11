package student.model.User;

import java.util.*;

public class UserArrayRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public UserArrayRepository() {
        users.add(new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"));
        users.add(new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Jamila"));
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User findUserByName(String name) {
        return users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
