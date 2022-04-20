package com.example.deliveryservice.database.Order;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.deliveryservice.database.Courier.Courier;
import com.example.deliveryservice.database.Courier.CourierContract;

public class OrderConnector {
    SQLiteOpenHelper databaseHelper;


    public OrderConnector(SQLiteOpenHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void createTable(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + OrderContract.TABLE_NAME + "("
                + OrderContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + OrderContract.COLUMN_NAME + " TEXT, "
                + OrderContract.COLUMN_COURIER_ID + " INTEGER, "
                + "FOREIGN KEY(" + OrderContract.COLUMN_COURIER_ID + ") REFERENCES " + CourierContract.TABLE_NAME + "(id))";

        database.execSQL(SQL_CREATE_TABLE);
    }

    public void dropTable(SQLiteDatabase database) {
        String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + OrderContract.TABLE_NAME;
        database.execSQL(SQL_DROP_TABLE);
    }

    public Order create(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderContract.COLUMN_COURIER_ID, order.courierId);
        values.put(OrderContract.COLUMN_NAME, order.name);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long id = database.insert(OrderContract.TABLE_NAME, null, values);

        database.close();

        return findById((int) id);
    }

    public void delete(int id) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] selectionArgs = new String[]{Integer.toString(id)};

        database.delete(OrderContract.TABLE_NAME, OrderContract.COLUMN_ID + "= ?", selectionArgs);
        database.close();
    }

    public void deleteAllForCourier(int courierId) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] selectionArgs = new String[]{Integer.toString(courierId)};

        database.delete(OrderContract.TABLE_NAME, OrderContract.COLUMN_COURIER_ID + "= ?", selectionArgs);
        database.close();
    }

    public void update(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderContract.COLUMN_COURIER_ID, order.courierId);
        values.put(OrderContract.COLUMN_NAME, order.name);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] selectionArgs = new String[]{Integer.toString(order.id)};

        database.update(OrderContract.TABLE_NAME, values, OrderContract.COLUMN_ID + "= ?", selectionArgs);
        database.close();
    }

    public Order findById(int id) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        try {
            String[] projection = {
                    OrderContract.COLUMN_COURIER_ID,
                    OrderContract.COLUMN_NAME,
            };
            String[] selectionArgs = {Long.toString(id)};
            String selection = OrderContract.COLUMN_ID + "=?";

            Cursor cursor = database.query(
                    OrderContract.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            cursor.moveToFirst();

            @SuppressLint("Range") Order order = new Order(id,
                    cursor.getInt(cursor.getColumnIndex(OrderContract.COLUMN_COURIER_ID)),
                    cursor.getString(cursor.getColumnIndex(OrderContract.COLUMN_NAME)));

            cursor.close();

            return order;
        } catch (Exception exception) {
            return null;
        } finally {
            database.close();
        }
    }

    public Cursor findAllForCourierCursor(int courierId) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] projection = {
                OrderContract.COLUMN_ID,
                OrderContract.COLUMN_COURIER_ID,
                OrderContract.COLUMN_NAME
        };

        String[] selectionArgs = {Long.toString(courierId)};
        String selection = OrderContract.COLUMN_COURIER_ID + "=?";

        Cursor cursor = database.query(
                OrderContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        cursor.moveToFirst();
        database.close();

        return cursor;
    }
}
