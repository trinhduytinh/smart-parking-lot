package com.example.diary1311.model;

public class Post {
    private String id;
    private String bienso;
    private String cccd;
    private String hoten;

    public Post(){}

    public Post(String id, String bienso, String cccd, String hoten) {
        this.id = id;
        this.bienso = bienso;
        this.cccd = cccd;
        this.hoten = hoten;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBienso() {
        return bienso;
    }

    public void setBienso(String bienso) {
        this.bienso = bienso;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }
}
