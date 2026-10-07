package com.example.businessmanager.presentation.book_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.businessmanager.domain.model.Book
import com.example.businessmanager.presentation.book_list.components.AuthorText
import com.example.businessmanager.presentation.book_list.components.TitleText

@Composable
fun BookDetailsContent(
    innerPadding: PaddingValues,
    book: Book
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(8.dp)
    ) {
        TitleText(
            title = book.title
        )
        AuthorText(
            author = book.author
        )
    }
}