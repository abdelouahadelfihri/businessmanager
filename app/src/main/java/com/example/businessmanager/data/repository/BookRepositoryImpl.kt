package com.example.businessmanager.data.repository

import com.example.businessmanager.data.dao.BookDao
import com.example.businessmanager.domain.model.Book
import com.example.businessmanager.domain.repository.BookRepository

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {
    override fun getBookList() = bookDao.getBookList()

    override suspend fun getBookById(id: Int) = bookDao.getBookById(id)

    override suspend fun insertBook(book: Book) = bookDao.insertBook(book)

    override suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    override suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
}