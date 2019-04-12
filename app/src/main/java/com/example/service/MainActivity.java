package com.example.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * bindService方式启动一个Service，需要重写onBind（）方法，会返回IBinder对象，实现IBinder接口的三种方式
 * 1、继承BInder类 2、使用Messenger类3、使用AIDL
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
    }

    private void initButton() {
        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //继承Binder类
                startActivity(new Intent(MainActivity.this,BindingActivity.class));
            }
        });
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用Messenger类
                startActivity(new Intent(MainActivity.this,MessengerActivity.class));
            }
        });
        findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用AIDL
            }
        });
    }
}
