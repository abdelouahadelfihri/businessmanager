package com.example.businessmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.businessmanager.core.BOOK_TABLE
import com.example.businessmanager.navigation.BookDetails

@Entity(tableName = BOOK_TABLE)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val author: String
)

fun Book.toBookDetails() = BookDetails(
    id = this.id,
    title = this.title,
    author = this.author
)