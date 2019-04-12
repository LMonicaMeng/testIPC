// BookManager.aidl
package com.example.serviceapp;
import com.example.serviceapp.Book;

// Declare any non-default types here with import statements

interface BookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     List<Book> getBooks();

     void addBook(in Book book);

}
