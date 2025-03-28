package org.example.users;

import org.example.users.ILoginable;

public abstract class User implements ILoginable {
    protected String username;
    protected String password;
    protected String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public void login() {} //FIXME

    @Override
    public void logout() {} // FIXME
}