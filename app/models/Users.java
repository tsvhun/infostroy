package models;

import javax.persistence.*;

/**
 * Created by Alexander on 13.02.2016.
 */

@Entity
public class Users {
    @Id
    @Column(name = "USER_ID")
    private int id;

    @Column(name="USERNAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    public Users() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
