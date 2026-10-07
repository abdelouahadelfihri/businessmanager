package com.example.businessmanager.presentation.book_details

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.businessmanager.domain.model.Book
import com.example.businessmanager.presentation.book_details.components.BookDetailsContent
import com.example.businessmanager.presentation.book_details.components.BookDetailsTopBar

@Composable
fun BookDetailsScreen(
    book: Book,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BookDetailsTopBar(
                onArrowBackIconClick = navigateBack
            )
        },
        content = { innerPadding ->
            BookDetailsContent(
                innerPadding = innerPadding,
                book = book
            )
        }
    )
}