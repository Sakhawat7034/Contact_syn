package com.example.sakhawat.myfirstapp.Models;

import java.util.ArrayList;

public class UserInfo {

    String name;
    String phn;
    String age;
    UserInfo(){

    }

    public UserInfo(String name, String phn, String age) {
        this.name = name;
        this.phn = phn;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
