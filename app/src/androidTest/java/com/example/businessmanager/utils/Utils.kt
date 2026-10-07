package com.example.businessmanager.utils

import android.content.Context
import com.example.businessmanager.R
import com.example.businessmanager.domain.model.Book

fun getBookTest(context: Context): Book {
    return Book(
        id = 1,
        title = context.getString(R.string.title_test),
        author = context.getString(R.string.author_test)
    )
}

fun getUpdatedBookTest(context: Context): Book {
    return getBookTest(context).copy(
        title = context.getString(R.string.new_title_test)
    )
}