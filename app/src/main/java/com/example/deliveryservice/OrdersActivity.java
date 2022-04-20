package com.example.deliveryservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.deliveryservice.database.DataBaseHelper;
import com.example.deliveryservice.database.Order.OrderContract;

public class OrdersActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    Integer courierId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dataBaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("courierId");
        courierId = Integer.parseInt(id);

        configureCreateButton();
        configureBackButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectAdapter();
    }

    private void configureBackButton() {
        Button toolbarButtonLeft = findViewById(R.id.toolbar_btn_left);
        toolbarButtonLeft.setVisibility(View.VISIBLE);
        toolbarButtonLeft.setEnabled(true);
        toolbarButtonLeft.setText(getResources().getString(R.string.back_btn_label));

        toolbarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configureCreateButton() {
        Button toolbarButtonRight = findViewById(R.id.toolbar_btn_right);
        toolbarButtonRight.setVisibility(View.VISIBLE);
        toolbarButtonRight.setEnabled(true);
        toolbarButtonRight.setText(getResources().getString(R.string.create_btn_label));

        toolbarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdersActivity.this, OrderActivity.class);
                intent.putExtra("type", "CREATE");
                intent.putExtra("courierId", Integer.toString(courierId));
                startActivity(intent);
            }
        });
    }


    private void connectAdapter() {
        SimpleCursorAdapter couriersAdapter = new SimpleCursorAdapter(
                this,
                R.layout.order_list_item,
                dataBaseHelper.orderConnector.findAllForCourierCursor(courierId),
                new String[]{
                        OrderContract.COLUMN_ID,
                        OrderContract.COLUMN_COURIER_ID,
                        OrderContract.COLUMN_NAME
                },
                new int[]{
                        R.id.order_id,
                        R.id.order_courier_id,
                        R.id.order_name
                },
                0);


        ListView listView = findViewById(R.id.orders_list);
        listView.setAdapter(couriersAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewID = view.findViewById(R.id.order_id);
                String orderId = viewID.getText().toString();

                Intent intent = new Intent(OrdersActivity.this, OrderActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("courierId", Integer.toString(courierId));
                intent.putExtra("type", "UPDATE");
                startActivity(intent);
            }
        });
    }
}