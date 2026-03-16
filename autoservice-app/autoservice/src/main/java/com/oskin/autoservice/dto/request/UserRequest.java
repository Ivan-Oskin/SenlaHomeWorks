package com.oskin.autoservice.dto.request;

public class UserRequest {
    String login;
    String password;

    public String getLogin() {
        return login;
    }

    public UserRequest() {

    }


    public UserRequest(String login, String password) {
        this.login = login;
        this.password = password;
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
