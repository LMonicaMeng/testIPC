package com.example.clientapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.example.serviceapp.Book;
import com.example.serviceapp.BookManager;

import java.util.ArrayList;
import java.util.List;

public class AIDLActivity extends Activity {
    private static final String TAG = AIDLActivity.class.getName();
    private static final String SERVICE_ACTION = "com.example.serviceapp.AIDLService";
    private boolean mBound;
    private BookManager bookManager = null;
    private List<Book> mBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_bind_aidl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound){
                    tryToBindService();
                    Log.e(TAG, "onClick: 客户端试图连接服务端" );
                    return;
                }
                if(bookManager == null) return;
                Book book = new Book();
                book.setName("世界未解之谜");
                book.setPrice(60);
                mBooks.add(book);
                try {
                    bookManager.addBook(book);
                    Log.e(TAG, "onServiceConnected: 客户端添加的图书" + mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onServiceConnectedfail: " + e.getMessage());
                }
            }
        });
    }

    private void tryToBindService() {
        Intent intent = new Intent();
        intent.setAction(SERVICE_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager pm = getPackageManager();
        ResolveInfo info = pm.resolveService(intent, 0);
        if (info != null) {
            String serviceName = info.serviceInfo.name;
            String packageName = info.serviceInfo.packageName;
            ComponentName componentName = new ComponentName(packageName, serviceName);
            intent.setComponent(componentName);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "serviceConnect ");
            mBound = true;
            bookManager = BookManager.Stub.asInterface(service);

            if(bookManager !=null){
                try {
                    List<Book> books = bookManager.getBooks();
                    Log.e(TAG, "onServiceConnected: 获取到图书"+books.toString() );
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onServiceConnected: "+e.getMessage() );
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            Log.e(TAG, "onServiceDisconnected: ");
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
