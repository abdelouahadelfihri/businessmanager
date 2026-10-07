package com.example.businessmanager.presentation.book_list

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.businessmanager.R
import com.example.businessmanager.components.LoadingIndicator
import com.example.businessmanager.core.logMessage
import com.example.businessmanager.core.showSnackbarMessage
import com.example.businessmanager.core.showToastMessage
import com.example.businessmanager.domain.model.Book
import com.example.businessmanager.domain.model.Response
import com.example.businessmanager.presentation.book_list.components.BookListContent
import com.example.businessmanager.presentation.book_list.components.BookListTopBar
import com.example.businessmanager.presentation.book_list.components.EmptyBookListContent
import com.example.businessmanager.presentation.book_list.components.InsertBookAlertDialog
import com.example.businessmanager.presentation.book_list.components.InsertBookFloatingActionButton

@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    navigateToBookDetailsScreen: (Book) -> Unit
) {
    val context = LocalContext.current
    val resources = context.resources
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var openInsertBookDialog by remember { mutableStateOf(false) }
    val bookListResponse by viewModel.bookListState.collectAsStateWithLifecycle()
    val insertBookResponse by viewModel.insertBookState.collectAsStateWithLifecycle()
    val updateBookResponse by viewModel.updateBookState.collectAsStateWithLifecycle()
    val deleteBookResponse by viewModel.deleteBookState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BookListTopBar()
        },
        floatingActionButton = {
            InsertBookFloatingActionButton(
                onInsertBookFloatingActionButtonClick = {
                    openInsertBookDialog = true
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        when(val bookListResponse = bookListResponse) {
            is Response.Idle -> {}
            is Response.Loading -> LoadingIndicator()
            is Response.Success -> bookListResponse.data.let { bookList ->
                if (bookList.isEmpty()) {
                    EmptyBookListContent(
                        innerPadding = innerPadding
                    )
                } else {
                    BookListContent(
                        innerPadding = innerPadding,
                        bookList = bookList,
                        onBookCardClick = navigateToBookDetailsScreen,
                        onUpdateBook = { book ->
                            viewModel.updateBook(book)
                        },
                        onEmptyBookField = { bookField ->
                            showSnackbarMessage(
                                coroutineScope = coroutineScope,
                                snackbarHostState = snackbarHostState,
                                message = resources.getString(R.string.empty_book_field_message, bookField)
                            )
                        },
                        onDeleteBook = { bookId ->
                            viewModel.deleteBook(bookId)
                        },
                        onNoBookUpdates = {
                            showSnackbarMessage(
                                coroutineScope = coroutineScope,
                                snackbarHostState = snackbarHostState,
                                message = resources.getString(R.string.no_book_updates_message)
                            )
                        }
                    )
                }
            }
            is Response.Failure -> bookListResponse.e.message?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    logMessage(errorMessage)
                    showToastMessage(context, errorMessage)
                }
            }
        }
    }

    if (openInsertBookDialog) {
        InsertBookAlertDialog(
            onInsertBook = { book ->
                viewModel.insertBook(book)
            },
            onEmptyBookField = { emptyField ->
                showSnackbarMessage(
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = resources.getString(R.string.empty_book_field_message, emptyField)
                )
            },
            onInsertBookDialogCancel = {
                openInsertBookDialog = false
            }
        )
    }

    when(val insertBookResponse = insertBookResponse) {
        is Response.Idle -> {}
        is Response.Loading -> LoadingIndicator()
        is Response.Success -> LaunchedEffect(Unit) {
            showSnackbarMessage(
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                message = resources.getString(R.string.book_action_message, BookAction.ADDED)
            )
            viewModel.resetInsertBookState()
        }
        is Response.Failure -> insertBookResponse.e.message?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                logMessage(errorMessage)
                showToastMessage(context, errorMessage)
            }
        }
    }

    when(val updateBookResponse = updateBookResponse) {
        is Response.Idle -> {}
        is Response.Loading -> LoadingIndicator()
        is Response.Success -> LaunchedEffect(Unit) {
            showSnackbarMessage(
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                message = resources.getString(R.string.book_action_message, BookAction.UPDATED)
            )
            viewModel.resetUpdateBookState()
        }
        is Response.Failure -> updateBookResponse.e.message?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                logMessage(errorMessage)
                showToastMessage(context, errorMessage)
            }
        }
    }

    when(val deleteBookResponse = deleteBookResponse) {
        is Response.Idle -> {}
        is Response.Loading -> LoadingIndicator()
        is Response.Success -> LaunchedEffect(Unit) {
            showSnackbarMessage(
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                message = resources.getString(R.string.book_action_message, BookAction.DELETED)
            )
            viewModel.resetDeleteBookState()
        }
        is Response.Failure -> deleteBookResponse.e.message?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                logMessage(errorMessage)
                showToastMessage(context, errorMessage)
            }
        }
    }
}

enum class BookAction() {
    ADDED,
    UPDATED,
    DELETED
}