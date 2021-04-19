package com.example.sakhawat.myfirstapp.Models;

public class PhoneData {
    String name;
    String phoneno;

    public PhoneData() {

    }

    public PhoneData(String name, String phoneno) {
        this.name = name;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }


}
