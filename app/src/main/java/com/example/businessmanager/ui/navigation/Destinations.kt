package com.example.businessmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.RequestQuote
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.ui.graphics.vector.ImageVector

/** One entity (= one Room table + its DAO). `key` identifies it in routes and data sources. */
data class EntityTab(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val description: String = ""
)

/** One business chain (Sales, Purchases...). It is a top tab AND a drawer section. */
data class Module(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val description: String = "",
    val tabs: List<EntityTab> = emptyList()   // the entities of this chain
)

object Destinations {
    const val DASHBOARD = "dashboard"
    const val THIRD_PARTIES = "third_parties"
    const val PRODUCTS = "products"
    const val SALES = "sales"
    const val PURCHASES = "purchases"
    const val EXPENSES = "expenses"

    val modules = listOf(
        Module(DASHBOARD, "Dashboard", Icons.Outlined.Dashboard, "Overview of your business"),

        Module(
            THIRD_PARTIES, "Third parties", Icons.Outlined.Groups,
            "Customers, suppliers and their contacts",
            tabs = listOf(
                EntityTab("third_parties", "Third parties", Icons.Outlined.Groups,
                    "Customers and suppliers (companies)"),                      // ThirdParty
                EntityTab("contacts", "Contacts", Icons.Outlined.ContactPage,
                    "People you deal with at each company")                      // Contact
            )
        ),

        Module(
            PRODUCTS, "Products & stock", Icons.Outlined.Inventory2,
            "Catalog, warehouses and stock levels",
            tabs = listOf(
                EntityTab("products", "Products", Icons.Outlined.Inventory2,
                    "Products and services you sell or buy"),                    // Product
                EntityTab("warehouses", "Warehouses", Icons.Outlined.Warehouse,
                    "Places where your stock is stored"),                        // Warehouse
                EntityTab("stock", "Stock", Icons.Outlined.Layers,
                    "Quantity of each product per warehouse"),                   // ProductStock
                EntityTab("movements", "Movements", Icons.Outlined.SwapVert,
                    "History of stock in, out and transfers")                    // StockMovement
            )
        ),

        Module(
            SALES, "Sales", Icons.Outlined.Storefront,
            "From quote to payment",
            tabs = listOf(
                EntityTab("quotes", "Quotes", Icons.Outlined.RequestQuote,
                    "Offers sent to customers, with their lines"),               // Quote + QuoteLine
                EntityTab("customer_orders", "Orders", Icons.Outlined.ShoppingCart,
                    "Confirmed customer orders, with their lines"),              // CustomerOrder + lines
                EntityTab("invoices", "Invoices", Icons.Outlined.Receipt,
                    "Customer invoices, with their lines"),                      // Invoice + InvoiceLine
                EntityTab("payments_in", "Payments", Icons.Outlined.Payments,
                    "Money received from customers")                             // Payment (IN) + allocations
            )
        ),

        Module(
            PURCHASES, "Purchases", Icons.Outlined.LocalShipping,
            "From supplier order to payment",
            tabs = listOf(
                EntityTab("supplier_orders", "Orders", Icons.Outlined.ShoppingCart,
                    "Orders placed with suppliers, with their lines"),           // SupplierOrder + lines
                EntityTab("supplier_invoices", "Invoices", Icons.Outlined.Receipt,
                    "Supplier invoices, with their lines"),                      // SupplierInvoice + lines
                EntityTab("payments_out", "Payments", Icons.Outlined.Payments,
                    "Money paid to suppliers")                                   // Payment (OUT) + allocations
            )
        ),

        Module(
            EXPENSES, "Expenses", Icons.Outlined.AccountBalanceWallet,
            "Costs and expense reports",
            tabs = listOf(
                EntityTab("expenses", "Expense reports", Icons.Outlined.Summarize,
                    "Expense reports, with their lines")                         // ExpenseReport + ExpenseLine
            )
        )
    )

    fun entity(key: String): EntityTab? = modules.flatMap { it.tabs }.firstOrNull { it.key == key }

    fun indexOfRoute(route: String): Int = modules.indexOfFirst { it.route == route }.coerceAtLeast(0)
}
