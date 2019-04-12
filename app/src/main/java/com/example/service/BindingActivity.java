package com.example.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class BindingActivity extends Activity {
    private LocalService localService;
    private Boolean mBound;//设置flag判断是否绑定service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_extend_binder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNum = localService.getRandomNum();
                Toast.makeText(BindingActivity.this, randomNum+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, LocalService.class), conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder localBinder = (LocalService.LocalBinder) service;
            localService = localBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound){
            unbindService(conn);
            mBound = false;
        }
    }

}
