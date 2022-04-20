package com.example.deliveryservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.deliveryservice.database.Courier.CourierContract;
import com.example.deliveryservice.database.DataBaseHelper;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureActionBar();

        dataBaseHelper = new DataBaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectAdapter();
    }

    private void configureActionBar() {
        Toolbar toolbar = findViewById(R.id.main_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Button toolbarButtonRight = findViewById(R.id.toolbar_btn_right);
        toolbarButtonRight.setVisibility(View.VISIBLE);
        toolbarButtonRight.setEnabled(true);
        toolbarButtonRight.setText(getResources().getString(R.string.create_btn_label));

        toolbarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CourierActivity.class);
                intent.putExtra("type", "CREATE");
                startActivity(intent);
            }
        });
    }

    private void connectAdapter() {
        SimpleCursorAdapter couriersAdapter = new SimpleCursorAdapter(
                this,
                R.layout.courier_list_item,
                dataBaseHelper.courierConnector.findAllCursor(),
                new String[]{
                        CourierContract.COLUMN_ID,
                        CourierContract.COLUMN_NAME,
                        CourierContract.COLUMN_EMAIL,
                        CourierContract.COLUMN_PHONE,
                        CourierContract.COLUMN_BIRTH_DATE
                },
                new int[]{
                        R.id.courier_id,
                        R.id.courier_name,
                        R.id.courier_email,
                        R.id.courier_phone,
                        R.id.courier_birth_date},
                0);

        ListView listView = findViewById(R.id.couriers_list);
        listView.setAdapter(couriersAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewID = view.findViewById(R.id.courier_id);
                String courierId = viewID.getText().toString();

                Intent intent = new Intent(MainActivity.this, CourierActivity.class);
                intent.putExtra("courierId", courierId);
                intent.putExtra("type", "UPDATE");
                startActivity(intent);
            }
        });
    }
}