package com.example.serviceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_aidl_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启aidl service
                startService(new Intent(MainActivity.this,AIDLService.class));
            }
        });
        findViewById(R.id.btn_messenger_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启 messenger service
                startService(new Intent(MainActivity.this,MyService.class));
            }
        });
    }
}
