package com.example.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {
    private static final String TAG = AIDLService.class.getName();
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL生成的BookManager
    private final BookManager.Stub bookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (mBooks != null) {
                    Log.e(TAG, "getBooks:服务端获得的书 "+mBooks.toString() );
                    return mBooks;
                }
                return new ArrayList<>();
            }

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if(book == null){
                    book = new Book();
                }
                //尝试修改book的参数，主要为了观察其到客户端的反馈
                book.setPrice(1000);
                if(!mBooks.contains(book)){
                    mBooks.add(book);
                }
                Log.e(TAG, "addBook:服务端增加的图书 "+mBooks.toString() );
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setPrice(50);
        book.setName("c++");
        mBooks.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }
}
