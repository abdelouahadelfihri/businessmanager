package com.example.businessmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.businessmanager.domain.model.toBookDetails
import com.example.businessmanager.presentation.book_list.BookListScreen
import com.example.businessmanager.presentation.book_details.BookDetailsScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BookListScreen
    ) {
        composable<BookListScreen> {
            BookListScreen(
                navigateToBookDetailsScreen = { book ->
                    val bookDetails = book.toBookDetails()
                    navController.navigate(bookDetails)
                }
            )
        }
        composable<BookDetails> { entry ->
            val bookDetails = entry.toRoute<BookDetails>()
            val book = bookDetails.toBook()
            BookDetailsScreen(
                book = book,
                navigateBack = navController::navigateUp
            )
        }
    }
}