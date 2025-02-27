package com.scorpios.tokenauthentication.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Think
 * @Title: User
 * @ProjectName token-authentication
 * @Description: TODO
 * @date 2019/1/1815:47
 */
public class User implements Serializable{
    Long id;
    String username;
    String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password +
                '}';
    }
}
