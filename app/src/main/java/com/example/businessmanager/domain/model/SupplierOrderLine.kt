package com.example.bizapp.data.entity

import androidx.room.*

// llx_commande_fournisseurdet : supplier order lines
@Entity(
    tableName = "commande_fournisseurdet",
    foreignKeys = [
        ForeignKey(SupplierOrder::class, ["id"], ["fk_commande"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Product::class, ["id"], ["fk_product"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("fk_commande"), Index("fk_product")]
)
data class SupplierOrderLine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_commande") val orderId: Long,
    @ColumnInfo(name = "fk_product") val productId: Long? = null,
    val description: String? = null,
    val qty: Double = 1.0,
    @ColumnInfo(name = "subprice") val unitPrice: Double = 0.0,
    @ColumnInfo(name = "tva_tx") val vatRate: Double = 20.0,
    @ColumnInfo(name = "remise_percent") val discountPercent: Double = 0.0,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    val rang: Int = 0
)
