package com.flancer.flancer.beans;

import android.app.Application;

/**
 * Created by 11500613 on 17/10/2017.
 */

public class User extends Application {
    private String name;
    private String firstName;
    private String Email;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}