package com.example.coamaster.coamasteruser;

import android.app.Application;

public class SaveActivity extends Application {
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_pw;
    private String Token;
    private String Request;
    private String Ordermenu;
    private int Price;

    public String getOrdermenu() {
        return Ordermenu;
    }

    public void setOrdermenu(String ordermenu) {
        Ordermenu = ordermenu;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getToken() { return Token;  }

    public void setToken(String token) { this.Token = token;  }

    public String getUser_id(){ return user_id; }

    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public String getUser_name(){
        return user_name;
    }

    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public String getUser_email(){
        return user_email;
    }

    public void setUser_email(String user_email){
        this.user_email = user_email;
    }

    public String getUser_pw(){
        return user_pw;
    }

    public void setUser_pw(String user_pw){
        this.user_pw = user_pw;
    }
}
