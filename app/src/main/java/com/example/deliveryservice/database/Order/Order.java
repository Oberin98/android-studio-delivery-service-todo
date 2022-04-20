package com.example.deliveryservice.database.Order;

public class Order {
    public int id;
    public int courierId;
    public String name;

    public Order() {
    }

    public Order(int courierId, String name) {
        this.courierId = courierId;
        this.name = name;
    }

    public Order(int id, int courierId, String name) {
        this.id = id;
        this.courierId = courierId;
        this.name = name;
    }
}
