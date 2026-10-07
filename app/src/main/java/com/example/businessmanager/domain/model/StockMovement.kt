package com.example.bizapp.data.entity

import androidx.room.*

// llx_stock_mouvement : stock movements
@Entity(
    tableName = "stock_mouvement",
    foreignKeys = [ForeignKey(Product::class, ["id"], ["fk_product"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("fk_product"), Index("fk_entrepot")]
)
data class StockMovement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_product") val productId: Long,
    @ColumnInfo(name = "fk_entrepot") val warehouseId: Long,
    @ColumnInfo(name = "value") val quantity: Double,           // signed: + in, - out
    @ColumnInfo(name = "type_mouvement") val type: Int,         // 0 input, 1 output, 2 transfer in, 3 transfer out
    val price: Double = 0.0,
    val label: String? = null,
    @ColumnInfo(name = "datem") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "origintype") val originType: String? = null, // "facture", "order_supplier"...
    @ColumnInfo(name = "fk_origin") val originId: Long? = null
)
