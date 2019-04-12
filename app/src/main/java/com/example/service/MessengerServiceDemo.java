package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

/**
 * Messenger可以获得IBinder类的实例
 *
 */
public class MessengerServiceDemo extends Service {

    private static final int MSG_SAY_HELLO = 1;

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!I'm service", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    final Messenger messenger = new Messenger(new ServiceHandler());
}
