package student.model.User;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User with a unique identifier and a name.
 */
public class User {
    private UUID id;
    private String name;

    /**
     * Constructs a new User with the specified UUID and name.
     *
     * @param id   the unique identifier of the user
     * @param name the name of the user
     */
    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the UUID of the user
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the UUID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return a string containing the user's id and name
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two users are considered equal if their id and name are equal.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    /**
     * Returns a hash code value for the user.
     *
     * @return the hash code based on the user's id and name
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
