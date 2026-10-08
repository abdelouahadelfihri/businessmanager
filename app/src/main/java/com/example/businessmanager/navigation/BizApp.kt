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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bizapp.ui.screens.DashboardScreen
import com.example.bizapp.ui.screens.ModuleScreen
import kotlinx.coroutines.launch

/**
 * Root composable. The navigation chrome adapts to the window width:
 *  - Compact  (phones)          -> modal drawer (hamburger button)
 *  - Medium   (foldables)       -> navigation rail
 *  - Expanded (tablets/desktop) -> permanent drawer
 */
@Composable
fun BizApp(widthClass: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val current = Destinations.byRoute(backStack?.destination?.route)

    val navigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    when (widthClass) {
        WindowWidthSizeClass.Compact -> {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerContent(current.route) { route ->
                            navigate(route)
                            scope.launch { drawerState.close() }
                        }
                    }
                }
            ) {
                AppContent(current, widthClass, navController, navigate) {
                    scope.launch { drawerState.open() }
                }
            }
        }

        WindowWidthSizeClass.Medium -> {
            Row(Modifier.fillMaxSize()) {
                AppRail(current.route, navigate)
                AppContent(current, widthClass, navController, navigate, onMenuClick = null)
            }
        }

        else -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(Modifier.width(300.dp)) {
                        DrawerContent(current.route, navigate)
                    }
                }
            ) {
                AppContent(current, widthClass, navController, navigate, onMenuClick = null)
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Content: top bar + NavHost
// ---------------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent(
    current: Module,
    widthClass: WindowWidthSizeClass,
    navController: NavHostController,
    onNavigate: (String) -> Unit,
    onMenuClick: (() -> Unit)?
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            current.group.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(current.title, fontWeight = FontWeight.SemiBold)
                    }
                },
                navigationIcon = {
                    if (onMenuClick != null) {
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
            startDestination = Destinations.DASHBOARD,
            modifier = Modifier.padding(padding)
        ) {
            Destinations.modules.forEach { module ->
                composable(module.route) {
                    if (module.tabs.isEmpty()) {
                        DashboardScreen(widthClass, onNavigate)
                    } else {
                        ModuleScreen(module, onCreate = { key ->
                            // TODO: navigate to your create/edit screen for this entity
                            scope.launch { snackbar.showSnackbar("New \"$key\" - plug your editor screen here") }
                        })
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Drawer (compact modal + expanded permanent)
// ---------------------------------------------------------------------------------------------

@Composable
private fun DrawerContent(currentRoute: String, onNavigate: (String) -> Unit) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        DrawerHeader()
        Spacer(Modifier.height(12.dp))

        Destinations.grouped.forEach { (group, modules) ->
            Text(
                group,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 6.dp)
            )
            modules.forEach { m ->
                NavigationDrawerItem(
                    label = { Text(m.title) },
                    icon = { Icon(m.icon, contentDescription = null) },
                    selected = m.route == currentRoute,
                    onClick = { onNavigate(m.route) },
                    badge = if (m.tabs.size > 1) {
                        { Text("${m.tabs.size}", style = MaterialTheme.typography.labelMedium) }
                    } else null,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
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
// Rail (medium)
// ---------------------------------------------------------------------------------------------

@Composable
private fun AppRail(currentRoute: String, onNavigate: (String) -> Unit) {
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
            Destinations.modules.forEach { m ->
                NavigationRailItem(
                    selected = m.route == currentRoute,
                    onClick = { onNavigate(m.route) },
                    icon = { Icon(m.icon, contentDescription = m.title) },
                    label = { Text(m.title, maxLines = 1, style = MaterialTheme.typography.labelSmall) },
                    alwaysShowLabel = true,
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}
