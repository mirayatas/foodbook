package com.example.deneme;

public class Users {


    private String Username;
    private String Mail;
    private String ui;
    private String password;

    public Users() {
    }

    public Users(String user, String username) {
        this.Username=username;
        this.ui = user;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public void setUserid(String userid) {
        ui = userid;
    }

    public String getUserid() {
        return ui;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
