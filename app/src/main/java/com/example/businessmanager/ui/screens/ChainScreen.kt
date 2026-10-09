package com.example.businessmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.businessmanager.ui.navigation.EntityTab
import com.example.businessmanager.ui.navigation.Module

/**
 * The screen of one chain tab (Sales, Purchases, Products & stock...).
 * It shows a header and one card per entity of the chain.
 * Tap a card -> list of that entity. Tap "+" -> create a record.
 * The grid is responsive: 1 column on phones, 2-3 on tablets.
 */
@Composable
fun ChainScreen(
    chain: Module,
    onOpenEntity: (String) -> Unit,
    onCreate: (String) -> Unit,
    countFor: @Composable (String) -> Int = { 0 }   // plug DAO counts here
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { ChainHeader(chain) }

        items(chain.tabs, key = { it.key }) { tab ->
            EntityCard(
                tab = tab,
                count = countFor(tab.key),
                onOpen = { onOpenEntity(tab.key) },
                onAdd = { onCreate(tab.key) }
            )
        }
    }
}

@Composable
private fun ChainHeader(chain: Module) {
    val cs = MaterialTheme.colorScheme
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(cs.primary, cs.secondary)))
            .padding(20.dp)
    ) {
        val (icon, title, subtitle, count) = createRefs()

        Box(
            Modifier
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .size(52.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) { Icon(chain.icon, null, tint = Color.White, modifier = Modifier.size(28.dp)) }

        Text(
            chain.title,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(icon.end, 16.dp)
                end.linkTo(parent.end)
                top.linkTo(icon.top)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            chain.description,
            color = Color.White.copy(alpha = 0.88f),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(subtitle) {
                start.linkTo(title.start)
                end.linkTo(parent.end)
                top.linkTo(title.bottom, 2.dp)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            if (chain.tabs.size == 1) "1 section" else "${chain.tabs.size} sections",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .constrainAs(count) {
                    start.linkTo(parent.start)
                    top.linkTo(icon.bottom, 16.dp)
                }
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

/** One entity card laid out with ConstraintLayout: icon | title + description + count | add button. */
@Composable
private fun EntityCard(tab: EntityTab, count: Int, onOpen: () -> Unit, onAdd: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpen)
                .padding(16.dp)
        ) {
            val (icon, title, description, countChip, add) = createRefs()

            Box(
                Modifier
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) { Icon(tab.icon, null, tint = cs.onPrimaryContainer) }

            Text(
                tab.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(icon.end, 14.dp)
                    end.linkTo(add.start, 8.dp)
                    top.linkTo(icon.top)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                tab.description,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(description) {
                    start.linkTo(title.start)
                    end.linkTo(add.start, 8.dp)
                    top.linkTo(title.bottom, 2.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                if (count == 1) "1 record" else "$count records",
                style = MaterialTheme.typography.labelMedium,
                color = cs.onSurfaceVariant,
                modifier = Modifier
                    .constrainAs(countChip) {
                        start.linkTo(title.start)
                        top.linkTo(description.bottom, 10.dp)
                    }
                    .clip(RoundedCornerShape(50))
                    .background(cs.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            FilledTonalIconButton(
                onClick = onAdd,
                modifier = Modifier.constrainAs(add) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            ) { Icon(Icons.Outlined.Add, contentDescription = "Add ${tab.title}") }
        }
    }
}
