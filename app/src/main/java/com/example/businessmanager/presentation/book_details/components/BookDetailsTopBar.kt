package com.example.businessmanager.presentation.book_details.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.businessmanager.R
import com.example.businessmanager.components.ActionIconButton

@Composable
fun BookDetailsTopBar(
    onArrowBackIconClick: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = stringResource(
                    id = R.string.book_details_screen_title
                )
            )
        },
        navigationIcon = {
            ActionIconButton(
                onActionIconButtonClick = onArrowBackIconClick,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                resourceId = R.string.navigate_back
            )
        }
    )
}