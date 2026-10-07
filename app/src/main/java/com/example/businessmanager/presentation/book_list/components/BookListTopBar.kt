package com.example.businessmanager.presentation.book_list.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.businessmanager.R

@Composable
fun BookListTopBar() {
    TopAppBar (
        title = {
            Text(
                text = stringResource(
                    id = R.string.book_list_screen_title
                )
            )
        }
    )
}