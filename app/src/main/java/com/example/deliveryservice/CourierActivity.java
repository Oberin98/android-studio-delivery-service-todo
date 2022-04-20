package com.example.deliveryservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deliveryservice.database.Courier.Courier;
import com.example.deliveryservice.database.DataBaseHelper;

public class CourierActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    EditText nameInput, emailInput, phoneInput, birthDateInput;
    Integer courierId;
    Boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        dataBaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String id = intent.getStringExtra("courierId");

        if (type != null) isCreate = type.equals("CREATE");
        if (id != null) courierId = Integer.parseInt(id);

        nameInput = findViewById(R.id.courier_name_input);
        emailInput = findViewById(R.id.courier_email_input);
        phoneInput = findViewById(R.id.courier_phone_input);
        birthDateInput = findViewById(R.id.courier_birth_date_input);

        configureInputs();
        configureBackButton();
        configureActionButton();
        configureDeleteButton();
        configureShowOrdersButton();
    }

    private void configureInputs() {
        if (!isCreate && courierId != null) {
            Courier courier = dataBaseHelper.courierConnector.findById(courierId);

            if (courier != null) {
                nameInput.setText(courier.name);
                emailInput.setText(courier.email);
                phoneInput.setText(courier.phone);
                birthDateInput.setText(courier.birthDate);
            }
        }
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

    private void configureActionButton() {
        Button actionButton = findViewById(R.id.courier_action_btn);
        actionButton.setText(isCreate ? R.string.create_btn_label : R.string.update_btn_label);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Courier courier = new Courier();
                courier.name = nameInput.getText().toString();
                courier.email = emailInput.getText().toString();
                courier.phone = phoneInput.getText().toString();
                courier.birthDate = birthDateInput.getText().toString();

                if (isCreate) {
                    Courier newCourier = dataBaseHelper.courierConnector.create(courier);
                    isCreate = false;
                    courierId = newCourier.id;

                    configureInputs();
                    configureActionButton();
                    configureDeleteButton();
                    configureShowOrdersButton();
                } else {
                    courier.id = courierId;
                    dataBaseHelper.courierConnector.update(courier);
                }
            }
        });
    }

    private void configureDeleteButton() {
        Button deleteButton = findViewById(R.id.courier_delete_btn);

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
                dataBaseHelper.courierConnector.delete(courierId);
                dataBaseHelper.orderConnector.deleteAllForCourier(courierId);
                finish();
            }
        });
    }

    private void configureShowOrdersButton() {
        Button showOrdersButton = findViewById(R.id.show_orders_btn);

        if (isCreate) {
            showOrdersButton.setEnabled(false);
            showOrdersButton.setVisibility(View.GONE);
        } else {
            showOrdersButton.setEnabled(true);
            showOrdersButton.setVisibility(View.VISIBLE);
        }

        showOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourierActivity.this, OrdersActivity.class);
                intent.putExtra("courierId", Integer.toString(courierId));
                startActivity(intent);
            }
        });
    }
}