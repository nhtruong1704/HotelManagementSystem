package com.xuancanhit.hotelmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.AdminLoginActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.customer.CustomerLoginActivity;

public class MainActivity extends AppCompatActivity {
    //Time Splash before run app
    int splash_time = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, CustomerLoginActivity.class));
                finish();
            }
        }, splash_time);
    }
}