package com.example.freelancer;

import android.app.Application;

public class Token extends Application {
    public static String token="asd";

    public String getToken(){
        return this.token;
    }

    public void setToken(String d){
        this.token=d;
    }
}
