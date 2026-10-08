package com.example.businessmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.RequestQuote
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.businessmanager.ui.navigation.Destinations
import java.util.Calendar

/**
 * Responsive dashboard built with a decoupled ConstraintSet:
 *  - Compact : banner, KPIs in a 2x2 grid, quick actions, recent invoices (stacked)
 *  - Medium / Expanded : banner, 4 KPIs in one row, quick actions (38%) next to recent invoices (62%)
 * The composables only carry a layoutId; the arrangement lives in dashboardConstraints().
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(widthClass: WindowWidthSizeClass, onNavigate: (String) -> Unit) {
    val wide = widthClass != WindowWidthSizeClass.Compact
    val constraints = remember(wide) { dashboardConstraints(wide) }
    val cs = MaterialTheme.colorScheme

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        ConstraintLayout(constraintSet = constraints, modifier = Modifier.fillMaxWidth()) {
            WelcomeBanner(Modifier.layoutId("banner"))

            KpiCard(Modifier.layoutId("sales"), "Sales this month", "128,400 $CURRENCY", "+12.4% vs last month",
                Icons.Outlined.TrendingUp, cs.primary, cs.primaryContainer)
            KpiCard(Modifier.layoutId("purchases"), "Purchases", "74,950 $CURRENCY", "+3.1% vs last month",
                Icons.Outlined.TrendingDown, cs.secondary, cs.secondaryContainer)
            KpiCard(Modifier.layoutId("unpaid"), "Unpaid invoices", "21,830 $CURRENCY", "4 invoices overdue",
                Icons.Outlined.Receipt, cs.tertiary, cs.tertiaryContainer)
            KpiCard(Modifier.layoutId("lowStock"), "Low stock", "7 products", "2 out of stock",
                Icons.Outlined.Warning, Color(0xFFB3261E), Color(0xFFFFDAD6))

            QuickActions(Modifier.layoutId("actions"), onNavigate)
            RecentInvoices(Modifier.layoutId("recent")) { onNavigate(Destinations.SALES) }
        }
        Spacer(Modifier.height(24.dp))
    }
}

private fun dashboardConstraints(wide: Boolean) = ConstraintSet {
    val banner = createRefFor("banner")
    val sales = createRefFor("sales")
    val purchases = createRefFor("purchases")
    val unpaid = createRefFor("unpaid")
    val lowStock = createRefFor("lowStock")
    val actions = createRefFor("actions")
    val recent = createRefFor("recent")

    constrain(banner) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.fillToConstraints
    }

    if (!wide) {
        val mid = createGuidelineFromStart(0.5f)

        constrain(sales) {
            top.linkTo(banner.bottom); start.linkTo(parent.start); end.linkTo(mid)
            width = Dimension.fillToConstraints
        }
        constrain(purchases) {
            top.linkTo(banner.bottom); start.linkTo(mid); end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        val row1 = createBottomBarrier(sales, purchases)
        constrain(unpaid) {
            top.linkTo(row1); start.linkTo(parent.start); end.linkTo(mid)
            width = Dimension.fillToConstraints
        }
        constrain(lowStock) {
            top.linkTo(row1); start.linkTo(mid); end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        val row2 = createBottomBarrier(unpaid, lowStock)
        constrain(actions) {
            top.linkTo(row2); start.linkTo(parent.start); end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        constrain(recent) {
            top.linkTo(actions.bottom); start.linkTo(parent.start); end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
    } else {
        // 4 equal KPI cards in a single row
        createHorizontalChain(sales, purchases, unpaid, lowStock, chainStyle = ChainStyle.Spread)
        listOf(sales, purchases, unpaid, lowStock).forEach { kpi ->
            constrain(kpi) {
                top.linkTo(banner.bottom)
                width = Dimension.fillToConstraints
            }
        }
        val kpiBottom = createBottomBarrier(sales, purchases, unpaid, lowStock)
        val split = createGuidelineFromStart(0.38f)
        constrain(actions) {
            top.linkTo(kpiBottom); start.linkTo(parent.start); end.linkTo(split)
            width = Dimension.fillToConstraints
        }
        constrain(recent) {
            top.linkTo(kpiBottom); start.linkTo(split); end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------------------------

@Composable
private fun WelcomeBanner(modifier: Modifier) {
    val cs = MaterialTheme.colorScheme
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 18 -> "Good afternoon"
        else -> "Good evening"
    }

    Box(modifier.padding(6.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(cs.primary, cs.secondary)))
                .padding(20.dp)
        ) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (title, subtitle, icon) = createRefs()
                Text(
                    greeting,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(icon.start, 12.dp)
                        width = Dimension.fillToConstraints
                    }
                )
                Text(
                    "Here is your business at a glance.",
                    color = Color.White.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(subtitle) {
                        top.linkTo(title.bottom, 4.dp)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                        width = Dimension.fillToConstraints
                    }
                )
                Icon(
                    Icons.Outlined.Storefront, null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .constrainAs(icon) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(56.dp)
                )
            }
        }
    }
}

@Composable
private fun KpiCard(
    modifier: Modifier,
    label: String,
    value: String,
    note: String,
    icon: ImageVector,
    accent: Color,
    accentContainer: Color
) {
    Box(modifier.padding(6.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentContainer),
                    contentAlignment = Alignment.Center
                ) { Icon(icon, null, tint = accent) }
                Spacer(Modifier.height(12.dp))
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                Text(note, style = MaterialTheme.typography.labelSmall, color = accent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickActions(modifier: Modifier, onNavigate: (String) -> Unit) {
    val actions = listOf(
        Triple("New invoice", Icons.Outlined.Receipt, Destinations.SALES),
        Triple("New quote", Icons.Outlined.RequestQuote, Destinations.SALES),
        Triple("New order", Icons.Outlined.ShoppingCart, Destinations.SALES),
        Triple("Purchase", Icons.Outlined.LocalShipping, Destinations.PURCHASES),
        Triple("Add product", Icons.Outlined.Inventory2, Destinations.PRODUCTS),
        Triple("Add contact", Icons.Outlined.ContactPage, Destinations.THIRD_PARTIES),
        Triple("Add expense", Icons.Outlined.AccountBalanceWallet, Destinations.EXPENSES)
    )
    Box(modifier.padding(6.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Quick actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    actions.forEach { (label, icon, route) ->
                        AssistChip(
                            onClick = { onNavigate(route) },
                            label = { Text(label) },
                            leadingIcon = { Icon(icon, null, Modifier.size(18.dp)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentInvoices(modifier: Modifier, onSeeAll: () -> Unit) {
    val rows = remember { sampleRows("invoices") }
    Box(modifier.padding(6.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 8.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recent invoices",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onSeeAll) { Text("See all") }
                }
                rows.forEachIndexed { index, row ->
                    EntityRowItem(row, Icons.Outlined.Receipt)
                    if (index < rows.lastIndex) {
                        HorizontalDivider(
                            Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}
