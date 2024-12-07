package org.example.librarymanager.Model;

public class Student extends User {

    private final String studentNumber;
    private final String image;
    private final String _class;

    public Student(String studentNumber,
                   String password, String image,
                   String name, String _class) {
        super(name, password);
        this.studentNumber = studentNumber;
        this.image = image;
        this._class = _class;
    }

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public String getImage() {
        return this.image;
    }

    public String get_class() {
        return this._class;
    }

    // Implementation of abstract method
    @Override
    public String getUniqueIdentifier() {
        return this.studentNumber;
    }
}
