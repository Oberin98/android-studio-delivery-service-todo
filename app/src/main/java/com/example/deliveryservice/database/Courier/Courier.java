package com.example.deliveryservice.database.Courier;

public class Courier {
    public int id;
    public String name;
    public String email;
    public String phone;
    public String birthDate;

    public Courier() {
    }

    public Courier(String name, String email, String phone, String birthDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Courier(int id, String name, String email, String phone, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
