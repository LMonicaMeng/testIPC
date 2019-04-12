package com.example.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * 利用Messenger、Handler进行跨进程通信，此为服务端
 */
public class MyService extends Service {
    private Messenger clientMessenger;
    private static final int RECEIVE_SEND_MESSAGE = 0x0001;
    private static final int REPLY_TO_CLIENT = 0x0002;

    private class MyServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_SEND_MESSAGE:
                    Bundle data = msg.getData();
                    if (data != null) {
                        Log.e("IPC", "handleMessage:你好客户端，我是服务端 ");
                        clientMessenger =  msg.replyTo;
                        Message msgToClient = Message.obtain();
                        msgToClient.what = REPLY_TO_CLIENT;
                        //进程间通信使用bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("msg","你好客户端，我们现在可以交流了");
                        msgToClient.setData(bundle);
                        try {
                            clientMessenger.send(msgToClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            Log.e("IPC", e.getMessage() );
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger serviceMessenger = new Messenger(new MyServiceHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clientMessenger = null;
    }
}
