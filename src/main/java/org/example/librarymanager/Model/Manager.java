package org.example.librarymanager.Model;

public class Manager extends User {

    private final String managerNumber;

    public Manager(String managerNumber, String password, String name) {
        super(name, password);
        this.managerNumber = managerNumber;
    }

    public String getManagerNumber() {
        return this.managerNumber;
    }

    // Implementation of abstract method
    @Override
    public String getUniqueIdentifier() {
        return this.managerNumber;
    }
}
