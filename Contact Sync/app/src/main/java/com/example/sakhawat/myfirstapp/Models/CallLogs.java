package com.example.sakhawat.myfirstapp.Models;

public class CallLogs {

    String name;
    String number;
    String duration;
    String data;

    public CallLogs(String name, String number, String duration, String data) {
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
