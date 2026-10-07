package com.example.businessmanager.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.businessmanager.data.dao.BookDao
import com.example.businessmanager.domain.model.Book

@Database(
    entities = [Book::class],
    version = 1,
    exportSchema = false
)
abstract class BookDb : RoomDatabase() {
    abstract val bookDao: BookDao
}