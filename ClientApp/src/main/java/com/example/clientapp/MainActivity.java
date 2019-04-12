package com.example.clientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_aidl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AIDL方式进行进程间通信
                startActivity(new Intent(MainActivity.this,AIDLActivity.class));
            }
        });
        findViewById(R.id.btn_messenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //messenger方式进行进程间通信
                startActivity(new Intent(MainActivity.this,ClientActivity.class));
            }
        });
    }
}
