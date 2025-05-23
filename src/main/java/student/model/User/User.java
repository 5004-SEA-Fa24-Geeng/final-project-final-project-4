package student.model.User;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user with a unique ID and a name.
 */
public class User {
    private UUID id;
    private String name;

    /**
     * Constructs a new User with the specified ID and name.
     *
     * @param id   the unique identifier of the user
     * @param name the name of the user
     */
    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the user ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id the new user ID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the new user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string containing the user ID and name
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Compares this user to another object for equality based on ID and name.
     *
     * @param o the object to compare with
     * @return {@code true} if the users are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    /**
     * Returns a hash code based on the user's ID and name.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}