package com.example.deliveryservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.deliveryservice.database.Courier.Courier;
import com.example.deliveryservice.database.Courier.CourierConnector;
import com.example.deliveryservice.database.Order.Order;
import com.example.deliveryservice.database.Order.OrderConnector;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "delivery_service";

    public CourierConnector courierConnector;
    public OrderConnector orderConnector;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        courierConnector = new CourierConnector(this);
        orderConnector = new OrderConnector(this);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        courierConnector.createTable(database);
        orderConnector.createTable(database);
        insertMockData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int _1, int _2) {
        courierConnector.dropTable(database);
        orderConnector.dropTable(database);
        onCreate(database);
    }

    public void insertMockData() {
        Courier courier1 = courierConnector.create(new Courier("Some name 1", "someName1@gmail.com", "+421951999990", "01.01.1995"));
        Courier courier2 = courierConnector.create(new Courier("Some name 2", "someName2@gmail.com", "+421951999991", "02.01.1995"));
        Courier courier3 = courierConnector.create(new Courier("Some name 3", "someName3@gmail.com", "+421951999992", "03.01.1995"));
        Courier courier4 = courierConnector.create(new Courier("Some name 4", "someName4@gmail.com", "+421951999993", "04.01.1995"));

        orderConnector.create(new Order(courier1.id, "Order 1"));
        orderConnector.create(new Order(courier1.id, "Order 2"));
        orderConnector.create(new Order(courier1.id, "Order 3"));
        orderConnector.create(new Order(courier1.id, "Order 4"));

        orderConnector.create(new Order(courier2.id, "Order 1"));
        orderConnector.create(new Order(courier2.id, "Order 2"));
        orderConnector.create(new Order(courier2.id, "Order 3"));
        orderConnector.create(new Order(courier2.id, "Order 4"));

        orderConnector.create(new Order(courier3.id, "Order 1"));
        orderConnector.create(new Order(courier3.id, "Order 2"));
        orderConnector.create(new Order(courier3.id, "Order 3"));
        orderConnector.create(new Order(courier3.id, "Order 4"));

        orderConnector.create(new Order(courier4.id, "Order 1"));
        orderConnector.create(new Order(courier4.id, "Order 2"));
        orderConnector.create(new Order(courier4.id, "Order 3"));
        orderConnector.create(new Order(courier4.id, "Order 4"));
    }
}
