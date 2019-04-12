package com.example.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

/**
 * 此只是服务端单方面的通信，服务端未对客户端进行反馈
 * TODO 此处先做单方面通信处理，后续进行双方面通信处理
 */
public class MessengerActivity extends Activity {
    private Boolean mBound;
    private Messenger messenger = null;
    private static final int MSG_SAY_HELLO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_messenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayHello();
            }
        });
    }

    private void sayHello() {
        if(mBound) return;
        //发送一条消息给服务端
        Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.RESPOND_VIA_MESSAGE");
        intent.setPackage("com.example.service");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
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
