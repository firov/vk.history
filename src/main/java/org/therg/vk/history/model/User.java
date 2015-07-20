package org.therg.vk.history.model;

public class User {

    public User() {

    }

    public User(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public Long id;

    public String displayName;

    public String firstName;
    public String lastName;
}
