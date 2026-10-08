package com.example.businessmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.RequestQuote
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.ui.graphics.vector.ImageVector

/** One tab = one entity list (backed by one DAO). `key` is used to pick the data source. */
data class EntityTab(val key: String, val title: String, val icon: ImageVector)

/** One drawer / rail item. A module with several tabs shows a TabRow + swipeable pager. */
data class Module(
    val route: String,
    val title: String,
    val group: String,
    val icon: ImageVector,
    val tabs: List<EntityTab> = emptyList()
)

object Destinations {
    const val DASHBOARD = "dashboard"
    const val THIRD_PARTIES = "third_parties"
    const val PRODUCTS = "products"
    const val SALES = "sales"
    const val PURCHASES = "purchases"
    const val EXPENSES = "expenses"

    val modules = listOf(
        Module(DASHBOARD, "Dashboard", "Overview", Icons.Outlined.Dashboard),

        Module(
            THIRD_PARTIES, "Third parties", "Directory", Icons.Outlined.Groups,
            tabs = listOf(
                EntityTab("third_parties", "Third parties", Icons.Outlined.Groups),   // ThirdParty
                EntityTab("contacts", "Contacts", Icons.Outlined.ContactPage)          // Contact
            )
        ),

        Module(
            PRODUCTS, "Products & stock", "Catalog", Icons.Outlined.Inventory2,
            tabs = listOf(
                EntityTab("products", "Products", Icons.Outlined.Inventory2),          // Product
                EntityTab("warehouses", "Warehouses", Icons.Outlined.Warehouse),       // Warehouse
                EntityTab("stock", "Stock", Icons.Outlined.Layers),                    // ProductStock
                EntityTab("movements", "Movements", Icons.Outlined.SwapVert)           // StockMovement
            )
        ),

        Module(
            SALES, "Sales", "Trade", Icons.Outlined.Storefront,
            tabs = listOf(
                EntityTab("quotes", "Quotes", Icons.Outlined.RequestQuote),            // Quote (+ QuoteLine)
                EntityTab("customer_orders", "Orders", Icons.Outlined.ShoppingCart),   // CustomerOrder (+ lines)
                EntityTab("invoices", "Invoices", Icons.Outlined.Receipt),             // Invoice (+ InvoiceLine)
                EntityTab("payments_in", "Payments", Icons.Outlined.Payments)          // Payment direction = IN
            )
        ),

        Module(
            PURCHASES, "Purchases", "Trade", Icons.Outlined.LocalShipping,
            tabs = listOf(
                EntityTab("supplier_orders", "Orders", Icons.Outlined.ShoppingCart),   // SupplierOrder (+ lines)
                EntityTab("supplier_invoices", "Invoices", Icons.Outlined.Receipt),    // SupplierInvoice (+ lines)
                EntityTab("payments_out", "Payments", Icons.Outlined.Payments)         // Payment direction = OUT
            )
        ),

        Module(
            EXPENSES, "Expenses", "Finance", Icons.Outlined.AccountBalanceWallet,
            tabs = listOf(
                EntityTab("expenses", "Expense reports", Icons.Outlined.Summarize)     // ExpenseReport (+ ExpenseLine)
            )
        )
    )

    /** Group name -> modules, in declaration order (used for drawer section headers). */
    val grouped: Map<String, List<Module>> get() = modules.groupBy { it.group }

    fun byRoute(route: String?): Module = modules.firstOrNull { it.route == route } ?: modules.first()
}
