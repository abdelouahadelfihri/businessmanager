package com.example.businessmanager.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import kotlinx.coroutines.flow.Flow
import com.example.businessmanager.core.BOOK_TABLE
import com.example.businessmanager.domain.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM $BOOK_TABLE ORDER BY id ASC")
    fun getBookList(): Flow<List<Book>>

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id")
    suspend fun getBookById(id: Int): Book

    @Insert(onConflict = IGNORE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}