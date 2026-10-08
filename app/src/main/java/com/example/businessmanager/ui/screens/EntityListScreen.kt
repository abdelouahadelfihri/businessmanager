package com.example.businessmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.businessmanager.ui.navigation.EntityTab

/**
 * Generic list screen used by every tab.
 *  - `rows` comes from the DAO (empty by default -> shows the empty state).
 *  - No records at all : big icon + explanation + "Add record" button.
 *  - Search with no match : short "no results" message.
 *  - Responsive: the grid adds columns as the width grows.
 *
 * Wiring example (inside ModuleScreen's `rowsFor`):
 *   "invoices"    -> db.invoiceDao().observeAll().collectAsState(emptyList()).value.map { it.toRow() }
 *   "payments_in" -> db.paymentDao().observeByDirection("IN")...
 */
@Composable
fun EntityListScreen(
    tab: EntityTab,
    rows: List<EntityRow> = emptyList(),
    onCreate: (String) -> Unit
) {
    var query by rememberSaveable(tab.key) { mutableStateOf("") }
    val filtered = remember(query, rows) {
        if (query.isBlank()) rows
        else rows.filter { it.title.contains(query, true) || it.subtitle.contains(query, true) }
    }

    if (rows.isEmpty()) {
        // Nothing stored yet
        EmptyState(
            icon = tab.icon,
            title = "No ${tab.title.lowercase()} yet",
            message = "You have not added any record here. Start by adding your first one.",
            actionLabel = "Add record",
            onAction = { onCreate(tab.key) }
        )
        return
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search ${tab.title.lowercase()}") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )

            if (filtered.isEmpty()) {
                EmptyState(
                    icon = Icons.Outlined.Search,
                    title = "No results",
                    message = "Nothing matches \"$query\"."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 340.dp),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered) { row ->
                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            EntityRowItem(row, tab.icon, Modifier.clickable { /* TODO: open detail screen */ })
                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = { onCreate(tab.key) },
            icon = { Icon(Icons.Outlined.Add, null) },
            text = { Text("New") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

/** One list row laid out with ConstraintLayout: avatar | title + subtitle | amount + status chip. */
@Composable
fun EntityRowItem(row: EntityRow, icon: ImageVector, modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        val (avatar, title, subtitle, amount, status) = createRefs()

        Box(
            Modifier
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }

        Text(
            row.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(avatar.end, 12.dp)
                end.linkTo(amount.start, 8.dp)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            row.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(subtitle) {
                start.linkTo(title.start)
                end.linkTo(status.start, 8.dp)
                top.linkTo(title.bottom, 3.dp)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            row.amount.orEmpty(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.constrainAs(amount) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        )

        if (row.status != null) {
            StatusChip(
                row.status, row.tone,
                Modifier.constrainAs(status) {
                    end.linkTo(parent.end)
                    top.linkTo(amount.bottom, 6.dp)
                }
            )
        } else {
            Box(Modifier.constrainAs(status) {
                end.linkTo(parent.end)
                top.linkTo(amount.bottom)
            })
        }
    }
}

@Composable
fun StatusChip(text: String, tone: Tone, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    val (bg, fg) = when (tone) {
        Tone.Success -> Color(0xFFDDF5E4) to Color(0xFF1B6B3A)
        Tone.Warning -> Color(0xFFFFEFD0) to Color(0xFF8A5A00)
        Tone.Danger -> Color(0xFFFFDAD6) to Color(0xFF93000A)
        Tone.Neutral -> cs.surfaceVariant to cs.onSurfaceVariant
    }
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = fg,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

/** Reusable empty state: tinted icon, title, message and an optional action button. */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .widthIn(max = 360.dp)
        )
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Icon(Icons.Outlined.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(actionLabel)
            }
        }
    }
}
