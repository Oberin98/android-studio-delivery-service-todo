package com.example.deliveryservice.database.Courier;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourierConnector {
    SQLiteOpenHelper databaseHelper;


    public CourierConnector(SQLiteOpenHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void createTable(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + CourierContract.TABLE_NAME + "("
                + CourierContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + CourierContract.COLUMN_NAME + " TEXT, "
                + CourierContract.COLUMN_EMAIL + " TEXT, "
                + CourierContract.COLUMN_PHONE + " TEXT, "
                + CourierContract.COLUMN_BIRTH_DATE + " TEXT )";

        database.execSQL(SQL_CREATE_TABLE);
    }

    public void dropTable(SQLiteDatabase database) {
        String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + CourierContract.TABLE_NAME;

        database.execSQL(SQL_DROP_TABLE);
    }

    public Courier create(Courier courier) {
        ContentValues values = new ContentValues();
        values.put(CourierContract.COLUMN_NAME, courier.name);
        values.put(CourierContract.COLUMN_EMAIL, courier.email);
        values.put(CourierContract.COLUMN_PHONE, courier.phone);
        values.put(CourierContract.COLUMN_BIRTH_DATE, courier.birthDate);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long id = database.insert(CourierContract.TABLE_NAME, null, values);

        database.close();

        return findById((int) id);
    }

    public void delete(int id) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] selectionArgs = new String[]{Long.toString(id)};

        database.delete(CourierContract.TABLE_NAME, CourierContract.COLUMN_ID + "= ?", selectionArgs);
        database.close();
    }

    public void update(Courier courier) {
        ContentValues values = new ContentValues();
        values.put(CourierContract.COLUMN_NAME, courier.name);
        values.put(CourierContract.COLUMN_EMAIL, courier.email);
        values.put(CourierContract.COLUMN_PHONE, courier.phone);
        values.put(CourierContract.COLUMN_BIRTH_DATE, courier.birthDate);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] selectionArgs = new String[]{"" + courier.id};

        database.update(CourierContract.TABLE_NAME, values, CourierContract.COLUMN_ID + "= ?", selectionArgs);
        database.close();
    }

    public Courier findById(int id) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        try {
            String[] projection = {
                    CourierContract.COLUMN_NAME,
                    CourierContract.COLUMN_EMAIL,
                    CourierContract.COLUMN_PHONE,
                    CourierContract.COLUMN_BIRTH_DATE
            };
            String[] selectionArgs = {Long.toString(id)};
            String selection = CourierContract.COLUMN_ID + "=?";

            Cursor cursor = database.query(
                    CourierContract.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            cursor.moveToFirst();

            @SuppressLint("Range") Courier courier = new Courier(id,
                    cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_BIRTH_DATE)));

            cursor.close();

            return courier;
        } catch (Exception exception) {
            return null;
        } finally {
            database.close();
        }
    }

    @SuppressLint("Range")
    public List<HashMap<String, String>> findAll() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + CourierContract.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_ID)));
                map.put("name", cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_NAME)));
                map.put("email", cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_EMAIL)));
                map.put("phone", cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_PHONE)));
                map.put("birthDate", cursor.getString(cursor.getColumnIndex(CourierContract.COLUMN_BIRTH_DATE)));
                list.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return list;
    }

    public Cursor findAllCursor() {
        String SQL_FIND_ALL = "SELECT * FROM " + CourierContract.TABLE_NAME;

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(SQL_FIND_ALL, null);

        cursor.moveToFirst();
        database.close();

        return cursor;
    }

}
