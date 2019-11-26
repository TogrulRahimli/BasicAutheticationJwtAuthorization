package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInfo {

    private List<User> users = new ArrayList<>();

    {
        users.add(new User(1, "Togrul", "togrul", "togrul123"));
        users.add(new User(2, "Taleh", "taleh", "taleh123"));
        users.add(new User(3, "Farhad", "farhad", "farhad123"));
    }


    public User authUser(String username, String password) {
        return users.stream().filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }
}
