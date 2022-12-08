package com.example.attendance;

public class Student {

    private String ID;
    private String name;
    private String status;


    public Student(String name, String ID) {
        this.ID = ID;
        this.name = name;
        this.status = "Present";
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
