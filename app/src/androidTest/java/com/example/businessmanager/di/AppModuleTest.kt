package com.example.businessmanager.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import com.example.businessmanager.data.network.BookDb
import com.example.businessmanager.domain.repository.BookRepository
import com.example.businessmanager.presentation.book_list.BookListViewModel
import com.example.businessmanager.data.repository.FakeBookRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class AppModuleTest {
    @Provides
    fun provideBookDb() = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext<Context>(),
        BookDb::class.java
    ).build()

    @Provides
    fun provideBookDao(
        bookDb: BookDb
    ) = bookDb.bookDao

    @Provides
    fun provideBookRepository(): BookRepository = FakeBookRepositoryImpl()

    @Provides
    fun provideBookListViewModel(
        repo: BookRepository
    ) = BookListViewModel(repo)
}