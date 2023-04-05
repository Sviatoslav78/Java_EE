package com.example.my_demo.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    private Long id;
    private String login;
    private String password;

    public void setId(Long id) {
        this.id = id;
    }

    public User(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
