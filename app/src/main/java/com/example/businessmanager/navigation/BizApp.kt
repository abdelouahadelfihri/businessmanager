package com.example.businessmanager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.businessmanager.ui.screens.ChainScreen
import com.example.businessmanager.ui.screens.DashboardScreen
import com.example.businessmanager.ui.screens.EntityListScreen
import com.example.businessmanager.ui.screens.EntityRow
import kotlinx.coroutines.launch

private const val HOME = "home"
private const val ENTITY_ROUTE = "entity/{key}"
private fun entityRoute(key: String) = "entity/$key"

/**
 * Navigation model
 *  - Top tabs + drawer sections = the business chains (Dashboard, Third parties, Products & stock,
 *    Sales, Purchases, Expenses). Swipe or tap a tab to change chain.
 *  - Each chain tab shows a screen with one card per entity of that chain.
 *  - A card opens the list of that entity (full screen, back arrow).
 *  - The drawer lists every chain AND its entities, so any entity is one tap away.
 *
 * Responsive chrome:  Compact -> modal drawer | Medium -> rail | Expanded -> permanent drawer.
 *
 * Data hooks (empty by default -> empty states are shown):
 *   rowsFor(entityKey)  rows of an entity list      countFor(entityKey)  record count on the card
 */
@Composable
fun BizApp(
    widthClass: WindowWidthSizeClass,
    rowsFor: @Composable (String) -> List<EntityRow> = { emptyList() },
    countFor: @Composable (String) -> Int = { 0 },
    recentInvoices: List<EntityRow> = emptyList()
) {
    val navController = rememberNavController()
    val chains = Destinations.modules
    val pagerState = rememberPagerState(pageCount = { chains.size })
    val scope = rememberCoroutineScope()

    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val entity: EntityTab? =
        if (route == ENTITY_ROUTE) backStack?.arguments?.getString("key")?.let { Destinations.entity(it) } else null

    val openChain: (Int) -> Unit = { index ->
        if (entity != null) navController.popBackStack(HOME, inclusive = false)
        scope.launch { pagerState.animateScrollToPage(index) }
    }
    val openEntity: (String) -> Unit = { key ->
        navController.navigate(entityRoute(key)) {
            popUpTo(HOME)
            launchSingleTop = true
        }
    }

    when (widthClass) {
        WindowWidthSizeClass.Compact -> {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerContent(
                            chains = chains,
                            selectedChain = pagerState.currentPage,
                            selectedEntity = entity?.key,
                            onChain = { openChain(it); scope.launch { drawerState.close() } },
                            onEntity = { openEntity(it); scope.launch { drawerState.close() } }
                        )
                    }
                }
            ) {
                AppContent(
                    widthClass, navController, pagerState, entity, openEntity,
                    rowsFor, countFor, recentInvoices,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        }

        WindowWidthSizeClass.Medium -> {
            Row(Modifier.fillMaxSize()) {
                AppRail(chains, pagerState.currentPage, entity == null, openChain)
                AppContent(
                    widthClass, navController, pagerState, entity, openEntity,
                    rowsFor, countFor, recentInvoices, onMenuClick = null
                )
            }
        }

        else -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(Modifier.width(300.dp)) {
                        DrawerContent(chains, pagerState.currentPage, entity?.key, openChain, openEntity)
                    }
                }
            ) {
                AppContent(
                    widthClass, navController, pagerState, entity, openEntity,
                    rowsFor, countFor, recentInvoices, onMenuClick = null
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Content: top bar + NavHost (home with chain tabs | entity list)
// ---------------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent(
    widthClass: WindowWidthSizeClass,
    navController: NavHostController,
    pagerState: PagerState,
    entity: EntityTab?,
    openEntity: (String) -> Unit,
    rowsFor: @Composable (String) -> List<EntityRow>,
    countFor: @Composable (String) -> Int,
    recentInvoices: List<EntityRow>,
    onMenuClick: (() -> Unit)?
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val chains = Destinations.modules

    val onCreate: (String) -> Unit = { key ->
        // TODO: navigate to your create/edit screen for this entity
        scope.launch { snackbar.showSnackbar("New \"$key\" - plug your editor screen here") }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = {
                    if (entity != null) {
                        Text(entity.title, fontWeight = FontWeight.SemiBold)
                    } else {
                        Column {
                            Text(
                                "BIZMANAGER",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(chains[pagerState.currentPage].title, fontWeight = FontWeight.SemiBold)
                        }
                    }
                },
                navigationIcon = {
                    if (entity != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back")
                        }
                    } else if (onMenuClick != null) {
                        IconButton(onClick = onMenuClick) { Icon(Icons.Outlined.Menu, "Open menu") }
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Search, "Search") }
                    IconButton(onClick = {}) {
                        BadgedBox(badge = { Badge { Text("3") } }) {
                            Icon(Icons.Outlined.Notifications, "Notifications")
                        }
                    }
                    Box(
                        Modifier
                            .padding(horizontal = 8.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(HOME) {
                HomeScreen(
                    widthClass = widthClass,
                    pagerState = pagerState,
                    onOpenEntity = openEntity,
                    onCreate = onCreate,
                    countFor = countFor,
                    recentInvoices = recentInvoices
                )
            }
            composable(
                route = ENTITY_ROUTE,
                arguments = listOf(navArgument("key") { type = NavType.StringType })
            ) { entry ->
                val tab = entry.arguments?.getString("key")?.let { Destinations.entity(it) }
                if (tab != null) {
                    EntityListScreen(tab = tab, rows = rowsFor(tab.key), onCreate = onCreate)
                }
            }
        }
    }
}

/** Chain tabs on top + swipeable pager of chain screens. */
@Composable
private fun HomeScreen(
    widthClass: WindowWidthSizeClass,
    pagerState: PagerState,
    onOpenEntity: (String) -> Unit,
    onCreate: (String) -> Unit,
    countFor: @Composable (String) -> Int,
    recentInvoices: List<EntityRow>
) {
    val chains = Destinations.modules
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            chains.forEachIndexed { index, chain ->
                LeadingIconTab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(chain.title) },
                    icon = { Icon(chain.icon, contentDescription = null) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val chain = chains[page]
            if (chain.tabs.isEmpty()) {
                DashboardScreen(
                    widthClass = widthClass,
                    onNavigate = { route ->
                        scope.launch { pagerState.animateScrollToPage(Destinations.indexOfRoute(route)) }
                    },
                    recentInvoices = recentInvoices
                )
            } else {
                ChainScreen(chain = chain, onOpenEntity = onOpenEntity, onCreate = onCreate, countFor = countFor)
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Drawer (compact modal + expanded permanent): chains with their entities
// ---------------------------------------------------------------------------------------------

@Composable
private fun DrawerContent(
    chains: List<Module>,
    selectedChain: Int,
    selectedEntity: String?,
    onChain: (Int) -> Unit,
    onEntity: (String) -> Unit
) {
    val colors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        DrawerHeader()
        Spacer(Modifier.height(12.dp))

        chains.forEachIndexed { index, chain ->
            NavigationDrawerItem(
                label = { Text(chain.title, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(chain.icon, contentDescription = null) },
                selected = selectedEntity == null && selectedChain == index,
                onClick = { onChain(index) },
                colors = colors,
                modifier = Modifier.padding(top = 6.dp)
            )
            chain.tabs.forEach { tab ->
                NavigationDrawerItem(
                    label = { Text(tab.title, style = MaterialTheme.typography.bodyMedium) },
                    icon = { Icon(tab.icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    selected = selectedEntity == tab.key,
                    onClick = { onEntity(tab.key) },
                    colors = colors,
                    modifier = Modifier.padding(start = 28.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DrawerHeader() {
    val cs = MaterialTheme.colorScheme
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(cs.primary, cs.secondary)))
            .padding(20.dp)
    ) {
        val (logo, name, tagline) = createRefs()
        Box(
            Modifier
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Storefront, null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
        Text(
            "BizManager",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(logo.bottom, 14.dp)
                start.linkTo(parent.start)
            }
        )
        Text(
            "Business management",
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.constrainAs(tagline) {
                top.linkTo(name.bottom, 2.dp)
                start.linkTo(parent.start)
            }
        )
    }
}

// ---------------------------------------------------------------------------------------------
// Rail (medium): one item per chain
// ---------------------------------------------------------------------------------------------

@Composable
private fun AppRail(
    chains: List<Module>,
    selectedChain: Int,
    onHome: Boolean,
    onChain: (Int) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            Icon(
                Icons.Outlined.Storefront,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp).size(32.dp)
            )
        }
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            chains.forEachIndexed { index, chain ->
                NavigationRailItem(
                    selected = onHome && selectedChain == index,
                    onClick = { onChain(index) },
                    icon = { Icon(chain.icon, contentDescription = chain.title) },
                    label = { Text(chain.title, maxLines = 1, style = MaterialTheme.typography.labelSmall) },
                    alwaysShowLabel = true,
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}
