package com.example.clientapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * 利用Messenger、Handler进行跨进程通信，此为客户端
 */
public class ClientActivity extends Activity {
    private static final String SERVICE_ACTION = "com.example.serviceapp.MyService";
    private boolean mBound;
    private Messenger serviceMessenger = null;
    private static final int RECEIVE_SEND_MESSAGE = 0x0001;
    private static final int REPLY_TO_CLIENT = 0x0002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        initEvent();
    }

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPLY_TO_CLIENT:
                    Bundle data = msg.getData();
                    if (data != null) {
                        String msg1 = data.getString("msg");
                        Log.e("IPC", "handleMessage: 客户端接收到服务端反馈的消息" + msg1);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger clientMessenger = new Messenger(new ClientHandler());

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            mBound = true;
            Message msg = Message.obtain();
            msg.what = RECEIVE_SEND_MESSAGE;
            //发送消息给服务端
            Bundle bundle = new Bundle();
            bundle.putString("msg", "服务端你好，我是客户端");
            msg.setData(bundle);
            msg.replyTo =clientMessenger;
            try {
                serviceMessenger.send(msg);
                Log.e("IPC", "onServiceConnected: 发送消息给服务端" + msg);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("IPC", "onServiceConnectedfalse: "+e.getMessage() );
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger = null;
            mBound = false;
            Log.e("IPC", "onServiceDisconnected: ");
        }
    };

    private void initEvent() {
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    //绑定service
                    Intent intent = new Intent();
                    intent.setAction(SERVICE_ACTION);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    PackageManager pm = getPackageManager();
                    ResolveInfo info = pm.resolveService(intent, 0);
                    if (info != null) {
                        String packageName = info.serviceInfo.packageName;
                        String name = info.serviceInfo.name;
                        ComponentName componentName = new ComponentName(packageName, name);
                        intent.setComponent(componentName);
                        try {
                            bindService(intent, conn, Context.BIND_AUTO_CREATE);
                            Log.e("IPC", "onClick: 客户端绑定服务");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("IPC", "onClick: " + e.getMessage());
                        }
                    }
                }
            }
        });
        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    unbindService(conn);
                    mBound = false;
                }
            }
        });
    }
}
