package com.example.businessmanager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.businessmanager.R
import com.example.businessmanager.data.dao.BookDao
import com.example.businessmanager.data.network.BookDb
import com.example.businessmanager.data.repository.BookRepositoryImpl
import com.example.businessmanager.domain.repository.BookRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBookDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        BookDb::class.java,
        context.resources.getString(R.string.db_name)
    ).build()

    @Provides
    fun provideBookDao(
        bookDb: BookDb
    ) = bookDb.bookDao

    @Provides
    fun provideBookRepository(
        bookDao: BookDao
    ): BookRepository = BookRepositoryImpl(
        bookDao = bookDao
    )
}