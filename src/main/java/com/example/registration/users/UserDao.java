package com.example.registration.users;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User findByName(String username) {
        return users.get(username);
    }
}
