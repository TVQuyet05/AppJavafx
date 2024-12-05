package org.example.librarymanager.Model;

public abstract class User {

    private final String name;
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    // Abstract method for getting a unique identifier
    public abstract String getUniqueIdentifier();

}
