package com.example.deneme;

import com.google.firebase.database.DataSnapshot;

public class Tarif {
    String baslik;
    String message;
    String username;
    String messageid;
    String userid;

    public Tarif(){}

    public Tarif(String baslik, String message, String userid, String username, String messageid) {
        this.baslik = baslik;
        this.message = message;
        this.userid = userid;
        this.username = username;
        this.messageid=messageid;

    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String  username) {
        this.username = username;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}
