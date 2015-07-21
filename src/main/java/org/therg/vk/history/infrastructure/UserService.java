package org.therg.vk.history.infrastructure;

import org.therg.vk.history.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    Map<Long, User> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    public void setupUsers(Map<Long, User> users) {
        this.users = users;
    }

    public User getUser(Long id) {
        User user = this.users.get(id);
        if (user == null) {
            user = new User(id, String.valueOf(id));
            this.users.put(id, user);
        }

        return user;
    }

}
