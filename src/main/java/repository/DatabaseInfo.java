package repository;

import model.User;

import java.util.Arrays;
import java.util.List;

public class DatabaseInfo {

    private List<User> users = Arrays.asList(
            new User(1, "Togrul", "togrul", "togrul123"),
            new User(2, "Taleh", "taleh", "taleh123"),
            new User(3, "Farhad", "farhad", "farhad123")
    );


    public User authUser(String username, String password) {
        return users.stream().filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }
}
