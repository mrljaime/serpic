package com.tilatina.guardcheck.Dao;

/**
 * Created by jaime on 11/04/16.
 */
public class User {
    private int id;
    private String name;

    public User(){}

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
