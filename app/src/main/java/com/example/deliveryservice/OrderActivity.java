package com.example.deliveryservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deliveryservice.database.Courier.Courier;
import com.example.deliveryservice.database.DataBaseHelper;
import com.example.deliveryservice.database.Order.Order;

public class OrderActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    EditText nameInput;
    Integer orderId, courierId;
    Boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dataBaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");
        String courierId = intent.getStringExtra("courierId");
        String type = intent.getStringExtra("type");

        if (orderId != null) this.orderId = Integer.parseInt(orderId);
        if (courierId != null) this.courierId = Integer.parseInt(courierId);
        if (type != null) this.isCreate = type.equals("CREATE");

        nameInput = findViewById(R.id.order_name_input);

        configureInputs();
        configureBackButton();
        configureActionButton();
        configureDeleteButton();
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

    private void configureInputs() {
        if (!isCreate && orderId != null) {
            Order order = dataBaseHelper.orderConnector.findById(orderId);

            if (order != null) {
                nameInput.setText(order.name);
            }
        }
    }

    private void configureActionButton() {
        Button actionButton = findViewById(R.id.order_action_btn);
        actionButton.setText(isCreate ? R.string.create_btn_label : R.string.update_btn_label);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.courierId = courierId;
                order.name = nameInput.getText().toString();

                if (isCreate) {
                    Order newOrder = dataBaseHelper.orderConnector.create(order);
                    isCreate = false;
                    orderId = newOrder.id;

                    configureInputs();
                    configureActionButton();
                    configureDeleteButton();
                } else {
                    order.id = orderId;
                    dataBaseHelper.orderConnector.update(order);
                }
            }
        });
    }

    private void configureDeleteButton() {
        Button deleteButton = findViewById(R.id.order_delete_btn);

        if (isCreate) {
            deleteButton.setEnabled(false);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            deleteButton.setEnabled(true);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.error));
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.orderConnector.delete(orderId);
                finish();
            }
        });
    }
}