package com.example.bizapp.data.entity

import androidx.room.*

// llx_facture_fourn_det : supplier invoice lines
@Entity(
    tableName = "facture_fourn_det",
    foreignKeys = [
        ForeignKey(SupplierInvoice::class, ["id"], ["fk_facture_fourn"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Product::class, ["id"], ["fk_product"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("fk_facture_fourn"), Index("fk_product")]
)
data class SupplierInvoiceLine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_facture_fourn") val invoiceId: Long,
    @ColumnInfo(name = "fk_product") val productId: Long? = null,
    val description: String? = null,
    val qty: Double = 1.0,
    @ColumnInfo(name = "pu_ht") val unitPrice: Double = 0.0,
    @ColumnInfo(name = "tva_tx") val vatRate: Double = 20.0,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0
)
