package com.example.businessmanager.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.businessmanager.domain.model.Book

interface BookRepository {
    fun getBookList(): Flow<List<Book>>

    suspend fun getBookById(id: Int): Book?

    suspend fun insertBook(book: Book)

    suspend fun updateBook(book: Book)

    suspend fun deleteBook(book: Book)
}