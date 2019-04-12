package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalService extends Service {
    private final IBinder localBinder = new LocalBinder();
    private Random random = new Random();
    //通过继承Binder的方式获得IBinder的对象
    public class LocalBinder extends Binder {
        LocalService getService(){
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public int getRandomNum(){
        return random.nextInt(100);
    }
}
