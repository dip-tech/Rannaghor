package com.example.rannaghor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //======================= SET STATUS BAR COLOR && STATUS BAR THEME ====================
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        //======================= ATTACH ORDER DETAILS FRAGMENT ============================
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_order_details,new OrderDetailsFragment()).commit();

    }
}