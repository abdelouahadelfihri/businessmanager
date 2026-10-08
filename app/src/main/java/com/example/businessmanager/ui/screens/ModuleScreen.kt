package com.example.businessmanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.businessmanager.ui.navigation.Module
import kotlinx.coroutines.launch

/**
 * A module = tabs (one per entity) + a swipeable pager.
 * Tapping a tab or swiping horizontally changes the entity list.
 * A single-tab module (Expenses) hides the tab row.
 */
@Composable
fun ModuleScreen(
    module: Module,
    onCreate: (String) -> Unit,
    rowsFor: @Composable (String) -> List<EntityRow> = { emptyList() } // plug DAO flows here
) {
    val pagerState = rememberPagerState(pageCount = { module.tabs.size })
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        if (module.tabs.size > 1) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                module.tabs.forEachIndexed { index, tab ->
                    LeadingIconTab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(tab.title) },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            key = { module.tabs[it].key }
        ) { page ->
            val tab = module.tabs[page]
            EntityListScreen(tab = tab, rows = rowsFor(tab.key), onCreate = onCreate)
        }
    }
}
